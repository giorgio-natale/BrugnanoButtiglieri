package it.polimi.emall.cpms.chargingmanagementservice.model.booking;

import it.polimi.emall.cpms.chargingmanagementservice.model.booking.dto.BookingKafkaDto;
import it.polimi.emall.cpms.chargingmanagementservice.utils.IdAssignedManager;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Set;


@Service
public class BookingManager extends IdAssignedManager<Booking, Long, BookingKafkaDto> {

    private final BookingRepository bookingRepository;

    public BookingManager(BookingRepository crudRepository) {
        super(crudRepository);
        this.bookingRepository = crudRepository;
    }

    public Booking getCurrentBookingByTime(Long chargingStationId, Long chargingPointId, Long socketId){
        return bookingRepository.findCurrentBookingForDate(
                chargingStationId,
                chargingPointId,
                socketId,
                OffsetDateTime.now()
        ).orElseThrow();
    }

    public Booking getCurrentBookingByBookingStatus(Long chargingStationId, Long chargingPointId, Long socketId, BookingStatusEnum bookingStatus){
        return bookingRepository.findCurrentBookingForState(
                chargingStationId,
                chargingPointId,
                socketId,
                bookingStatus
        ).orElseThrow();
    }

    public Set<Booking> getBookingsByChargingStation(Long chargingStationId){
        return bookingRepository.findAllByChargingStationId(chargingStationId);
    }


    @Override
    protected Booking updateEntity(Booking currentState, BookingKafkaDto desiredState) {
        currentState.updateBooking(
                desiredState.bookingCode,
                desiredState.chargingStationId,
                desiredState.customerId,
                desiredState.chargingPointId,
                desiredState.socketId,
                desiredState.timeFrame,
                desiredState.bookingType,
                desiredState.status
        );
        return currentState;
    }

    @Override
    protected Booking createDefault(Long key) {
        return new Booking(key);
    }
}
