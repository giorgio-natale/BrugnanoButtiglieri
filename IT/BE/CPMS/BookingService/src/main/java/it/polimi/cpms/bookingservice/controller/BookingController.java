package it.polimi.cpms.bookingservice.controller;

import it.polimi.emall.cpms.bookingservice.generated.http.server.controller.BookingApi;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
public class BookingController implements BookingApi {
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
        BookingInAdvanceDto bookingInAdvanceDto = null;
        BookingOnTheFlyDto bookingOnTheFlyDto = null;
        if(bookingRequestDto instanceof BookingInAdvanceDto)
            bookingInAdvanceDto = ((BookingInAdvanceDto) bookingRequestDto);
        else if (bookingRequestDto instanceof BookingOnTheFlyDto)
            bookingOnTheFlyDto = ((BookingOnTheFlyDto) bookingRequestDto);
        else
            throw new IllegalArgumentException("Booking request can only be in advance or on the fly");

        return new ResponseEntity<>(new BookingDto(
                123L,
                1L,
                bookingInAdvanceDto != null ? bookingInAdvanceDto.getCustomerId() : bookingOnTheFlyDto.getCustomerId(),
                bookingInAdvanceDto != null ? bookingInAdvanceDto.getChargingStationId() : bookingOnTheFlyDto.getChargingStationId(),
                bookingInAdvanceDto != null ? 100L : bookingOnTheFlyDto.getChargingPointId(),
                bookingInAdvanceDto != null ? 2L : bookingOnTheFlyDto.getSocketId(),
                bookingInAdvanceDto != null ? bookingInAdvanceDto.getSocketType() : SocketTypeDto.FAST,
                bookingInAdvanceDto != null ? bookingInAdvanceDto.getTimeframe() : new TimeframeDto(OffsetDateTime.now())
                        .endInstant(OffsetDateTime.now().plus(1, ChronoUnit.DAYS)),
                bookingInAdvanceDto != null ? BookingTypeDto.IN_ADVANCE : BookingTypeDto.ON_THE_FLY
        ), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> putBookingStatus(Long bookingCode, BookingStatusDto bookingStatusDto) {
        return BookingApi.super.putBookingStatus(bookingCode, bookingStatusDto);
    }
}
