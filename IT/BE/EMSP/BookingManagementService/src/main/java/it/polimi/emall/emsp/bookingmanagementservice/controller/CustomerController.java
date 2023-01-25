package it.polimi.emall.emsp.bookingmanagementservice.controller;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.controller.CustomerApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.*;
import it.polimi.emall.emsp.bookingmanagementservice.usecases.BookAChargeUseCase;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
public class CustomerController implements CustomerApi {

    private final BookAChargeUseCase bookAChargeUseCase;

    public CustomerController(BookAChargeUseCase bookAChargeUseCase) {
        this.bookAChargeUseCase = bookAChargeUseCase;
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
        try {
            if(!BeanUtils.getProperty(bookingRequestDto, "customerId").equals(String.valueOf(customerId)))
                throw new IllegalArgumentException("Customer id mismatch between path parameter and request");
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalArgumentException("Request does not contain customerId");
        }
        return new ResponseEntity<>(bookAChargeUseCase.createBooking(bookingRequestDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> putBookingStatus(Long customerId, Long bookingCode, BookingStatusDto bookingStatusDto) {
        return CustomerApi.super.putBookingStatus(customerId, bookingCode, bookingStatusDto);
    }
}
