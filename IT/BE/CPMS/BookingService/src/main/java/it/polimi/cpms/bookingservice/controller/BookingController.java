package it.polimi.cpms.bookingservice.controller;

import it.polimi.cpms.bookingservice.usecase.BookAChargeUseCase;
import it.polimi.cpms.bookingservice.usecase.StartAChargeUseCase;
import it.polimi.emall.cpms.bookingservice.generated.http.server.controller.BookingApi;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController implements BookingApi {

    private final BookAChargeUseCase bookAChargeUseCase;
    private final StartAChargeUseCase startAChargeUseCase;

    public BookingController(BookAChargeUseCase bookAChargeUseCase, StartAChargeUseCase startAChargeUseCase) {
        this.bookAChargeUseCase = bookAChargeUseCase;
        this.startAChargeUseCase = startAChargeUseCase;
    }

    @Override
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return BookingApi.super.getAllBookings();
    }

    @Override
    public ResponseEntity<BookingDto> getBooking(Long bookingCode) {
        return BookingApi.super.getBooking(bookingCode);
    }

    @Override
    public ResponseEntity<BookingStatusDto> getBookingStatus(Long bookingCode) {
        return BookingApi.super.getBookingStatus(bookingCode);
    }

    @Override
    public ResponseEntity<BookingDto> postBooking(BookingRequestDto bookingRequestDto) {
        if(bookingRequestDto instanceof BookingInAdvanceDto) {
            BookingDto newBooking = bookAChargeUseCase.createBookingInAdvance(((BookingInAdvanceDto) bookingRequestDto));
            return new ResponseEntity<>(newBooking, HttpStatus.OK);
        }else if (bookingRequestDto instanceof BookingOnTheFlyDto){
            BookingDto newBooking = bookAChargeUseCase.createBookingOnTheFly(((BookingOnTheFlyDto) bookingRequestDto));
            return new ResponseEntity<>(newBooking, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @Override
    public ResponseEntity<Void> putBookingStatus(Long bookingCode, BookingStatusDto bookingStatusDto) {
        if(bookingStatusDto instanceof BookingStatusInProgressDto) {  //TODO: check also that the update comes from emsp
            startAChargeUseCase.startACharge(bookingCode);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return BookingApi.super.putBookingStatus(bookingCode, bookingStatusDto);
        }
    }
}
