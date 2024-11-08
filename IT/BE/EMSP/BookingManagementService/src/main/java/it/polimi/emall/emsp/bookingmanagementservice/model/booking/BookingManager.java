package it.polimi.emall.emsp.bookingmanagementservice.model.booking;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingStatusDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingTypeDto;
import it.polimi.emall.emsp.bookingmanagementservice.utils.IdAssignedManager;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingManager extends IdAssignedManager<Booking, Long, BookingDto> {

    private final BookingStatusManager bookingStatusManager;
    private final BookingRepository bookingRepository;

    public BookingManager(BookingRepository crudRepository, BookingStatusManager bookingStatusManager) {
        super(crudRepository);
        this.bookingStatusManager = bookingStatusManager;
        this.bookingRepository = crudRepository;
    }

    public void updateStatus(Booking booking, BookingStatusDto bookingStatusDto){
        bookingStatusManager.update(booking.getBookingStatus().getId(), bookingStatusDto);
    }

    public Set<Booking> getAllBookingForCustomer(Long customerId){
        return bookingRepository.findAllByCustomerId(customerId);
    }

    public Set<BookingStatus> getAllBookingStatusesForCustomer(Long customerId){
        return getAllBookingForCustomer(customerId)
                .stream()
                .map(Booking::getBookingStatus)
                .collect(Collectors.toSet());
    }

    public BookingStatus getBookingStatus(Long bookingId){
        return bookingStatusManager.getEntityByKey(bookingId);
    }
    @Override
    public Booking createNewAndUpdate(Long key, BookingDto dto) {
        Booking booking = super.createNewAndUpdate(key, dto);
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
                new TimeFrame(desiredState.getTimeframe().getStartInstant(), desiredState.getTimeframe().getEndInstant())

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
    protected Booking createDefault(Long key) {
        return new Booking(key);
    }
}
