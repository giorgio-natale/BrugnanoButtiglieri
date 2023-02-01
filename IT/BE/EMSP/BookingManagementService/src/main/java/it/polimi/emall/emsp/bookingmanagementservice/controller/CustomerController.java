package it.polimi.emall.emsp.bookingmanagementservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.controller.CustomerApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.*;
import it.polimi.emall.emsp.bookingmanagementservice.usecases.BookAChargeUseCase;
import it.polimi.emall.emsp.bookingmanagementservice.usecases.RegisterDeviceUseCase;
import it.polimi.emall.emsp.bookingmanagementservice.usecases.StartABookingUseCase;
import it.polimi.emall.emsp.bookingmanagementservice.utils.HttpRequestUtils;
import it.polimi.emall.emsp.bookingmanagementservice.utils.JwtHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

//TODO: check authorization token in all endpoints
@RestController
public class CustomerController implements CustomerApi {

    private final BookAChargeUseCase bookAChargeUseCase;
    private final StartABookingUseCase startABookingUseCase;

    private final RegisterDeviceUseCase registerDeviceUseCase;
    private final JwtHelper jwtHelper;

    public CustomerController(BookAChargeUseCase bookAChargeUseCase, StartABookingUseCase startABookingUseCase, RegisterDeviceUseCase registerDeviceUseCase, JwtHelper jwtHelper) {
        this.bookAChargeUseCase = bookAChargeUseCase;
        this.startABookingUseCase = startABookingUseCase;
        this.registerDeviceUseCase = registerDeviceUseCase;
        this.jwtHelper = jwtHelper;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Override
    public ResponseEntity<List<BookingDto>> getAllBookings(Long customerId) {
        if(!jwtHelper.buildTokenDto(HttpRequestUtils.getAuthorizationToken()).customerId.equals(customerId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(bookAChargeUseCase.getAllBookingsForCustomer(customerId), HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Override
    public ResponseEntity<BookingDto> getBooking(Long customerId, Long bookingCode) {
        if(!jwtHelper.buildTokenDto(HttpRequestUtils.getAuthorizationToken()).customerId.equals(customerId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(bookAChargeUseCase.getBookingForCustomer(customerId, bookingCode), HttpStatus.OK);

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Override
    public ResponseEntity<BookingStatusDto> getBookingStatus(Long customerId, Long bookingCode) {
        if(!jwtHelper.buildTokenDto(HttpRequestUtils.getAuthorizationToken()).customerId.equals(customerId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(bookAChargeUseCase.getBookingStatusForCustomer(customerId, bookingCode), HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Override
    public ResponseEntity<BookingDto> postBooking(Long customerId, BookingRequestDto bookingRequestDto) {
        if(!jwtHelper.buildTokenDto(HttpRequestUtils.getAuthorizationToken()).customerId.equals(customerId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            if(!BeanUtils.getProperty(bookingRequestDto, "customerId").equals(String.valueOf(customerId)))
                throw new IllegalArgumentException("Customer id mismatch between path parameter and request");
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalArgumentException("Request does not contain customerId");
        }
        return new ResponseEntity<>(bookAChargeUseCase.createBooking(bookingRequestDto), HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Override
    public ResponseEntity<List<BookingStatusDto>> getAllBookingStatuses(Long customerId) {
        if(!jwtHelper.buildTokenDto(HttpRequestUtils.getAuthorizationToken()).customerId.equals(customerId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(bookAChargeUseCase.getAllBookingStatusForCustomer(customerId), HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Override
    public ResponseEntity<Void> putBookingStatus(Long customerId, Long bookingCode, BookingStatusDto bookingStatusDto) {
        if(!jwtHelper.buildTokenDto(HttpRequestUtils.getAuthorizationToken()).customerId.equals(customerId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if(bookingStatusDto instanceof BookingStatusInProgressDto) {
            startABookingUseCase.startBooking(customerId, bookingCode);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @Override
    public ResponseEntity<Void> registerDevice(Long customerId, RegisterDeviceRequestDto registerDeviceRequestDto) {
        registerDeviceUseCase.registerDeviceManager(customerId, registerDeviceRequestDto.getExpoToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
