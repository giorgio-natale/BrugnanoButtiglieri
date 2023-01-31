package it.polimi.cpms.bookingservice.model.booking;

import it.polimi.cpms.bookingservice.model.chargingstation.ChargingPoint;
import it.polimi.cpms.bookingservice.model.chargingstation.ChargingStation;
import it.polimi.cpms.bookingservice.model.chargingstation.ChargingStationManager;
import it.polimi.cpms.bookingservice.model.chargingstation.Socket;
import it.polimi.cpms.bookingservice.utils.IdGeneratedManager;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.model.ChargingPointModeClientDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingStatusDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingTypeDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.SocketTypeDto;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingManager extends IdGeneratedManager<Booking, Long, BookingDto> {

    private final BookingStatusManager bookingStatusManager;
    private final BookingRepository bookingRepository;

    private final ChargingStationManager chargingStationManager;

    public BookingManager(BookingRepository crudRepository, BookingStatusManager bookingStatusManager, ChargingStationManager chargingStationManager) {
        super(crudRepository);
        this.bookingStatusManager = bookingStatusManager;
        this.bookingRepository = crudRepository;
        this.chargingStationManager = chargingStationManager;
    }

    public Pair<Long, Long> findFreeChargingPointAndSocketIdsForBookingInAdvance(
            Long chargingStationId,
            TimeFrame timeFrame,
            SocketTypeDto socketTypeDto
    ){
        Set<Booking> intersectingBookings = bookingRepository.findIntersectingBookingsInAdvance(chargingStationId, timeFrame);
        ChargingStation chargingStation = chargingStationManager.getById(chargingStationId);
        Map<Long, Socket> availableSockets = chargingStation.getChargingPoints()
                .stream()
                .filter(chargingPoint -> chargingPoint.getMode().equals(ChargingPointModeClientDto.IN_ADVANCE))
                .flatMap(chargingPoint -> chargingPoint.getSockets().stream())
                .filter(socket -> socket.getType() == socketTypeDto)
                .collect(Collectors.toMap(
                        Socket::getId,
                        socket -> socket
                ));
        Set<Long> occupiedSocketIds = intersectingBookings.stream().map(Booking::getSocketId).collect(Collectors.toSet());

        availableSockets.keySet().removeAll(occupiedSocketIds);

        Long selectedSocketId = availableSockets.keySet()
                .stream()
                .sorted()
                .findFirst()
                .orElseThrow(() -> new NoAvailableSocketException("Cannot find a suitable socket for your request"));

        Long selectedChargingPointId = chargingStationManager.findChargingPointOwningSocket(availableSockets.get(selectedSocketId)).getId();
        return Pair.of(selectedChargingPointId, selectedSocketId);

    }

    public Booking updateStatus(Booking booking, BookingStatusDto bookingStatusDto){
        bookingStatusManager.updateEntity(booking.getBookingStatus(), bookingStatusDto);
        return booking;
    }

    public boolean confirmAvailabilityForBookingOnTheFly(
            Long chargingStationId,
            Long chargingPointId,
            Long socketId
    ){
        boolean occupied = bookingRepository.isBookingOnTheFlyOccupied(
                chargingStationId,
                chargingPointId,
                socketId
        );
        try{
            ChargingStation chargingStation = chargingStationManager.getById(chargingStationId);
            Socket socket = chargingStationManager.getSocket(chargingStation, socketId);
            ChargingPoint chargingPoint = chargingStationManager.findChargingPointOwningSocket(socket);
            if(!chargingPoint.getId().equals(chargingPointId))
                throw new IllegalArgumentException();
        }catch(NoSuchElementException e){
            throw new IllegalArgumentException("Charging point and/or socket ids are not valid");
        }

        return !occupied;
    }

    public Set<BookingStatus> getAllBookingStatuses(){
        return bookingStatusManager.getAll();
    }

    @Override
    public Booking createNewAndUpdate(BookingDto dto) {
        Booking booking = super.createNewAndUpdate(dto);
        BookingStatus status = bookingStatusManager.createNew(booking.getId());
        booking.setBookingStatus(status);
        return booking;
    }

    @Override
    protected Booking updateEntity(Booking currentState, BookingDto desiredState) {
        currentState.updateBooking(
            desiredState.getBookingCode(),
            desiredState.getBookingType(),
                desiredState.getChargingStationId(),
                desiredState.getCustomerId(),
                desiredState.getChargingPointId(),
                desiredState.getSocketId(),
                desiredState.getSocketType(),
                desiredState.getBookingType() == BookingTypeDto.IN_ADVANCE ?
                        TimeFrame.getClosedTimeFrame(desiredState.getTimeframe().getStartInstant(), desiredState.getTimeframe().getEndInstant())
                        :
                        TimeFrame.getOpenTimeFrame(desiredState.getTimeframe().getStartInstant())
        );

        return currentState;
    }

    @Override
    protected void preDelete(Long key) {
        bookingRepository.findById(key).ifPresent(booking -> {
            bookingStatusManager.delete(booking.getBookingStatus().getId());
        });
    }

    @Override
    protected Booking createDefault() {
        return new Booking();
    }
}
