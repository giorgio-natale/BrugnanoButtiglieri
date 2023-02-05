package it.polimi.emall.emsp.bookingmanagementservice.usecases;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.endpoints.BookingApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingRequestClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingRequestDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingStatusDto;
import it.polimi.emall.emsp.bookingmanagementservice.mappers.BookingDtoMapper;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.Booking;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingStatus;
import it.polimi.emall.emsp.bookingmanagementservice.model.cpocatalog.CpoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BookAChargeUseCase {

    private final BookingApi bookingApi;
    private final TransactionTemplate transactionTemplate;
    private final BookingManager bookingManager;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BookAChargeUseCase(BookingApi bookingApi, PlatformTransactionManager transactionManager, BookingManager bookingManager) {
        this.bookingApi = bookingApi;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.bookingManager = bookingManager;
    }

    public BookingDto createBooking(BookingRequestDto bookingRequestDto){
        BookingRequestClientDto bookingRequestClientDto = BookingDtoMapper.buildBookingRequestClientDto(bookingRequestDto);
        BookingClientDto receivedBookingDto =
                bookingApi.postBookingWithResponseSpec(bookingRequestClientDto)
                    .onStatus(HttpStatusCode::is4xxClientError, e -> {
                            throw new NoAvailableSocketException("The booking you requested is not available. Try another one");
                    })
                    .bodyToMono(BookingClientDto.class)
                    .block();
        BookingDto bookingDto = BookingDtoMapper.buildBookingDto(receivedBookingDto);
        return transactionTemplate.execute(status -> {
            Booking booking = bookingManager.createNewAndUpdate(bookingDto.getBookingId(), bookingDto);
            return BookingDtoMapper.buildBookingDto(booking);
        });
    }

    @Transactional
    public BookingDto getBookingForCustomer(Long customerId, Long bookingId){
        Booking booking = bookingManager.getEntityByKey(bookingId);
        if(!booking.getCustomerId().equals(customerId))
            throw new NoSuchElementException(String.format("Cannot find booking #%d", bookingId));
        return BookingDtoMapper.buildBookingDto(booking);
    }

    @Transactional
    public List<BookingDto> getAllBookingsForCustomer(Long customerId){
        return bookingManager.getAllBookingForCustomer(customerId)
                .stream()
                .sorted(Comparator.comparingLong(Booking::getId))
                .map(BookingDtoMapper::buildBookingDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingStatusDto getBookingStatusForCustomer(Long customerId, Long bookingId){
        Booking booking = bookingManager.getEntityByKey(bookingId);
        if(!booking.getCustomerId().equals(customerId))
            throw new NoSuchElementException(String.format("Cannot find booking #%d", bookingId));
        return BookingDtoMapper.buildBookingStatusDto(booking.getBookingStatus());
    }

    @Transactional
    public List<BookingStatusDto> getAllBookingStatusForCustomer(Long customerId){
        return bookingManager.getAllBookingStatusesForCustomer(customerId)
                .stream()
                .sorted(Comparator.comparingLong(BookingStatus::getId))
                .map(BookingDtoMapper::buildBookingStatusDto)
                .collect(Collectors.toList());
    }
}
