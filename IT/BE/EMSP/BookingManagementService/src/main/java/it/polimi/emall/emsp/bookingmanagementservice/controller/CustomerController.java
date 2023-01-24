package it.polimi.emall.emsp.bookingmanagementservice.controller;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.endpoints.BookingApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingRequestClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.controller.CustomerApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController implements CustomerApi {

    private final BookingApi bookingApiEndPoint;


    @Autowired
    public CustomerController(BookingApi bookingApiEndPoint) {
        this.bookingApiEndPoint = bookingApiEndPoint;
    }

    @Override
    public ResponseEntity<List<BookingDto>> getAllBookings(Long customerId) {
        return CustomerApi.super.getAllBookings(customerId);
    }

    @Override
    public ResponseEntity<BookingDto> getBooking(Long customerId, Long bookingCode) {
        return CustomerApi.super.getBooking(customerId, bookingCode);
    }

    @Override
    public ResponseEntity<BookingStatusDto> getBookingStatus(Long customerId, Long bookingCode) {
        return CustomerApi.super.getBookingStatus(customerId, bookingCode);
    }

    @Override
    public ResponseEntity<BookingDto> postBooking(Long customerId, BookingRequestDto bookingRequestDto) {
        BookingRequestClientDto bookingRequestClientDto = new BookingRequestClientDto();
        BeanUtils.copyProperties(bookingRequestDto, bookingRequestClientDto);
        BookingClientDto booking = bookingApiEndPoint.postBooking(bookingRequestClientDto).block();
        BookingDto result = new BookingDto(null, null, null, null, null, null, null, null);
        assert booking != null;
        BeanUtils.copyProperties(booking, result);
        result.setSocketType(SocketTypeDto.fromValue(booking.getSocketType().getValue()));
        result.setBookingType(BookingTypeDto.fromValue(booking.getBookingType().getValue()));
        TimeframeDto timeframeDto = new TimeframeDto(null);
        BeanUtils.copyProperties(booking.getTimeframe(), timeframeDto);
        result.setTimeframe(timeframeDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> putBookingStatus(Long customerId, Long bookingCode, BookingStatusDto bookingStatusDto) {
        return CustomerApi.super.putBookingStatus(customerId, bookingCode, bookingStatusDto);
    }
}
