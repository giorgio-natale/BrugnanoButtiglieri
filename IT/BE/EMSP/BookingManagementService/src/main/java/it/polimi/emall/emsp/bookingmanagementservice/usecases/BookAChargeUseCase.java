package it.polimi.emall.emsp.bookingmanagementservice.usecases;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.endpoints.BookingApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingRequestClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingRequestDto;
import it.polimi.emall.emsp.bookingmanagementservice.mappers.BookingDtoMapper;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.Booking;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.emsp.bookingmanagementservice.model.cpocatalog.CpoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class BookAChargeUseCase {

    private final BookingApi bookingApi;
    private final TransactionTemplate transactionTemplate;
    private final BookingManager bookingManager;
    private final CpoManager cpoManager;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BookAChargeUseCase(BookingApi bookingApi, PlatformTransactionManager transactionManager, BookingManager bookingManager, CpoManager cpoManager) {
        this.bookingApi = bookingApi;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.bookingManager = bookingManager;
        this.cpoManager = cpoManager;
    }

    public BookingDto createBooking(BookingRequestDto bookingRequestDto){
        BookingRequestClientDto bookingRequestClientDto = BookingDtoMapper.buildBookingRequestClientDto(bookingRequestDto);
        String cpmsBasePath = cpoManager.getCpmsBasePathFromChargingStationId(bookingRequestClientDto.getChargingStationId());
        bookingApi.getApiClient().setBasePath(cpmsBasePath);
        BookingClientDto receivedBookingDto = bookingApi.postBooking(bookingRequestClientDto).block();
        BookingDto bookingDto = BookingDtoMapper.buildBookingDto(receivedBookingDto);
        return transactionTemplate.execute(status -> {
            Booking booking = bookingManager.createNewAndUpdate(bookingDto.getBookingId(), bookingDto);

            return BookingDtoMapper.buildBookingDto(booking);
        });
    }
}
