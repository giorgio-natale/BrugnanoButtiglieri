package it.polimi.emall.emsp.bookingmanagementservice.usecases;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.endpoints.BookingApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingStatusInProgressClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingStatusInProgressDto;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.Booking;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

@Service
public class StartABookingUseCase {
    private final BookingApi bookingApi;
    private final BookingManager bookingManager;
    private final TransactionTemplate transactionTemplate;

    public StartABookingUseCase(
            BookingApi bookingApi,
            BookingManager bookingManager,
            PlatformTransactionManager transactionManager
    ) {
        this.bookingApi = bookingApi;
        this.bookingManager = bookingManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void startBooking(Long customerId, Long bookingId){
        Booking booking = bookingManager.getEntityByKey(bookingId);
        if(!booking.getCustomerId().equals(customerId))
            throw new NoSuchElementException(String.format(
                    "Cannot find booking %d for customer %d",
                    bookingId,
                    customerId
            ));

        if(!booking.getBookingStatus().getBookingStatus().equals(BookingStatusEnum.BookingStatusPlanned)){
            throw new IllegalStateException(String.format(
                    "Cannot start booking %s because it is not planned",
                    booking.getBookingCode()
            ));
        }

        OffsetDateTime now = OffsetDateTime.now();
        if(now.isBefore(booking.getTimeFrame().getStartInstant())
            || now.isAfter(booking.getTimeFrame().getEndInstant())){
            throw new IllegalStateException(String.format(
                    "Cannot start booking %s because its timeframe %s does not include now (%s)",
                    booking.getBookingCode(),
                    booking.getTimeFrame(),
                    now
            ));
        }

        bookingApi.putBookingStatus(
                    bookingId,
                    new BookingStatusInProgressClientDto().bookingStatus(BookingStatusEnum.BookingStatusInProgress.name())
                )
                .block();

        transactionTemplate.execute(status -> {
            bookingManager.updateStatus(bookingManager.getEntityByKey(bookingId), new BookingStatusInProgressDto());
            return null;
        });
    }
}
