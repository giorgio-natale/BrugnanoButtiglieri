package it.polimi.emall.cpms.chargingmanagementservice.model.booking;

import it.polimi.emall.cpms.chargingmanagementservice.model.booking.dto.BookingKafkaDto;
import it.polimi.emall.cpms.chargingmanagementservice.utils.IdAssignedManager;
import org.springframework.stereotype.Service;


@Service
public class BookingManager extends IdAssignedManager<Booking, Long, BookingKafkaDto> {


    public BookingManager(BookingRepository crudRepository) {
        super(crudRepository);
    }



    @Override
    protected Booking updateEntity(Booking currentState, BookingKafkaDto desiredState) {
        currentState.updateBooking(
                desiredState.bookingCode,
                desiredState.chargingStationId,
                desiredState.customerId,
                desiredState.chargingPointId,
                desiredState.socketId,
                desiredState.status
        );
        return currentState;
    }

    @Override
    protected Booking createDefault(Long key) {
        return new Booking(key);
    }
}
