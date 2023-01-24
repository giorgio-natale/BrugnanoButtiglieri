package it.polimi.emall.emsp.bookingmanagementservice.controller;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.controller.CustomerApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingRequestDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.BookingStatusDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController implements CustomerApi {
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
        return CustomerApi.super.postBooking(customerId, bookingRequestDto);
    }

    @Override
    public ResponseEntity<Void> putBookingStatus(Long customerId, Long bookingCode, BookingStatusDto bookingStatusDto) {
        return CustomerApi.super.putBookingStatus(customerId, bookingCode, bookingStatusDto);
    }
}
