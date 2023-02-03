package it.polimi.emall.emsp.bookingmanagementservice.usecases;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.endpoints.BookingApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingStatusCancelledClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingStatusCancelledDto;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.Booking;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.NoSuchElementException;

@Service
public class CancelABookingUseCase {
    private final BookingApi bookingApi;
    private final BookingManager bookingManager;
    private final TransactionTemplate transactionTemplate;

    public CancelABookingUseCase(BookingApi bookingApi, BookingManager bookingManager, PlatformTransactionManager transactionManager) {
        this.bookingApi = bookingApi;
        this.bookingManager = bookingManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void cancelBooking(Long customerId, Long bookingId){
        Booking booking = bookingManager.getEntityByKey(bookingId);
        if(!booking.getCustomerId().equals(customerId))
            throw new NoSuchElementException(String.format(
                    "Cannot find booking %d for customer %d",
                    bookingId,
                    customerId
            ));

        if(!booking.getBookingStatus().getBookingStatus().equals(BookingStatusEnum.BookingStatusPlanned)){
            throw new IllegalStateException(String.format(
                    "Cannot close booking %s because it is in status %s",
                    booking.getBookingCode(),
                    booking.getBookingStatus().getBookingStatus()
            ));
        }

        bookingApi.putBookingStatus(
                        bookingId,
                        new BookingStatusCancelledClientDto().bookingStatus(BookingStatusEnum.BookingStatusCancelled.name())
                                .bookingId(bookingId)
                )
                .block();

        transactionTemplate.execute(status -> {
            bookingManager.updateStatus(bookingManager.getEntityByKey(bookingId), new BookingStatusCancelledDto(bookingId));
            return null;
        });
    }
}
