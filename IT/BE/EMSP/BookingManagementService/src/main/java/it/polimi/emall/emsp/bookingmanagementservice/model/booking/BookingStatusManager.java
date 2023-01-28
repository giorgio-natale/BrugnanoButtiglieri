package it.polimi.emall.emsp.bookingmanagementservice.model.booking;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.*;
import it.polimi.emall.emsp.bookingmanagementservice.utils.IdAssignedManager;
import it.polimi.emall.emsp.bookingmanagementservice.utils.IdGeneratedManager;
import org.springframework.stereotype.Service;

@Service
class BookingStatusManager extends IdAssignedManager<BookingStatus, Long, BookingStatusDto> {

    public BookingStatusManager(BookingStatusRepository bookingStatusRepository) {
        super(bookingStatusRepository);
    }

    @Override
    protected BookingStatus updateEntity(BookingStatus currentState, BookingStatusDto desiredState) {
        BookingStatusEnum bookingStatus;
        ProgressInformation progressInformation = null;
        if (!(desiredState instanceof BookingStatusInProgressDto) &&
                !(desiredState instanceof BookingStatusPlannedDto) &&
                !(desiredState instanceof BookingStatusCancelledDto) &&
                !(desiredState instanceof BookingStatusCompletedDto) &&
                !(desiredState instanceof BookingStatusExpiredDto)) {
            throw new IllegalArgumentException(String.format("Booking status %s not supported", desiredState.getBookingStatus()));
        }
        bookingStatus = BookingStatusEnum.valueOf(desiredState.getBookingStatus());
        if (desiredState instanceof BookingStatusInProgressDto)
            progressInformation =
                    new ProgressInformation(((BookingStatusInProgressDto) desiredState).getExpectedMinutesLeft());


        currentState.changeStatus(bookingStatus);
        currentState.changeProgressInformation(progressInformation);
        return currentState;
    }

    @Override
    protected BookingStatus createDefault(Long key) {
        return new BookingStatus(key);
    }
}
