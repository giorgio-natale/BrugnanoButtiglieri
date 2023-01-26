package it.polimi.cpms.bookingservice.model.booking;

import it.polimi.cpms.bookingservice.utils.IdAssignedManager;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingTypeDto;
import org.springframework.stereotype.Service;

@Service
public class BookingManager extends IdAssignedManager<Booking, Long, BookingDto> {

    private final BookingStatusManager bookingStatusManager;

    public BookingManager(BookingRepository crudRepository, BookingStatusManager bookingStatusManager) {
        super(crudRepository);
        this.bookingStatusManager = bookingStatusManager;
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
    protected Booking createDefault(Long key) {
        return new Booking(key);
    }
}
