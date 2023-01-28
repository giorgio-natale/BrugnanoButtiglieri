package it.polimi.cpms.bookingservice.usecase;

import it.polimi.cpms.bookingservice.mappers.BookingMapper;
import it.polimi.cpms.bookingservice.mappers.CommonMapper;
import it.polimi.cpms.bookingservice.model.booking.Booking;
import it.polimi.cpms.bookingservice.model.booking.BookingManager;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingInAdvanceDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingTypeDto;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookAChargeUseCase {
    private final BookingManager bookingManager;

    public BookAChargeUseCase(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BookingDto createBookingInAdvance(BookingInAdvanceDto bookingInAdvanceRequestDto){
        //TODO: get from the token the emsp id and user id. Check if user id from token matches with the one in the request
        Pair<Long, Long> freeChargingPointAndSocketIds = bookingManager.findFreeChargingPointAndSocketIdsForBookingInAdvance(
                bookingInAdvanceRequestDto.getChargingStationId(),
                CommonMapper.buildTimeFrame(bookingInAdvanceRequestDto.getTimeframe()),
                bookingInAdvanceRequestDto.getSocketType()
        );

        Long freeChargingPointId = freeChargingPointAndSocketIds.getFirst();
        Long freeSocketId = freeChargingPointAndSocketIds.getSecond();

        BookingDto newBookingDto = new BookingDto(
                null,
                "#TODO: genera",
                bookingInAdvanceRequestDto.getCustomerId(),
                bookingInAdvanceRequestDto.getChargingStationId(),
                freeChargingPointId,
                freeSocketId,
                bookingInAdvanceRequestDto.getSocketType(),
                bookingInAdvanceRequestDto.getTimeframe(),
                BookingTypeDto.IN_ADVANCE
        );
        Booking newBooking = bookingManager.createNewAndUpdate(newBookingDto);

        return BookingMapper.buildBookingDto(newBooking);

    }
}
