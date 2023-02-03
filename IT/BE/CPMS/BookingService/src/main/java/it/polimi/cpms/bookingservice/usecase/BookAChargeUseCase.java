package it.polimi.cpms.bookingservice.usecase;

import it.polimi.cpms.bookingservice.mappers.BookingMapper;
import it.polimi.cpms.bookingservice.mappers.CommonMapper;
import it.polimi.cpms.bookingservice.model.booking.Booking;
import it.polimi.cpms.bookingservice.model.booking.BookingManager;
import it.polimi.cpms.bookingservice.model.booking.NoAvailableSocketException;
import it.polimi.cpms.bookingservice.model.booking.event.BookingUpdatedEvent;
import it.polimi.cpms.bookingservice.model.chargingstation.ChargingStation;
import it.polimi.cpms.bookingservice.model.chargingstation.ChargingStationManager;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class BookAChargeUseCase {
    private final BookingManager bookingManager;
    private final ChargingStationManager chargingStationManager;

    private final ApplicationEventPublisher applicationEventPublisher;

    public BookAChargeUseCase(BookingManager bookingManager, ChargingStationManager chargingStationManager, ApplicationEventPublisher applicationEventPublisher) {
        this.bookingManager = bookingManager;
        this.chargingStationManager = chargingStationManager;
        this.applicationEventPublisher = applicationEventPublisher;
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
                "",
                bookingInAdvanceRequestDto.getCustomerId(),
                bookingInAdvanceRequestDto.getChargingStationId(),
                freeChargingPointId,
                freeSocketId,
                bookingInAdvanceRequestDto.getSocketType(),
                bookingInAdvanceRequestDto.getTimeframe(),
                BookingTypeDto.IN_ADVANCE
        );
        Booking newBooking = bookingManager.createNewAndUpdate(newBookingDto);
        applicationEventPublisher.publishEvent(new BookingUpdatedEvent(
                BookingMapper.buildBookingKafkaDto(newBooking)
        ));

        return BookingMapper.buildBookingDto(newBooking);

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BookingDto createBookingOnTheFly(BookingOnTheFlyDto bookingOnTheFlyRequestDto){
        //TODO: get from the token the emsp id and user id. Check if user id from token matches with the one in the request
        if(bookingManager.confirmAvailabilityForBookingOnTheFly(
                bookingOnTheFlyRequestDto.getChargingStationId(),
                bookingOnTheFlyRequestDto.getChargingPointId(),
                bookingOnTheFlyRequestDto.getSocketId())
        ){
            ChargingStation chargingStation = chargingStationManager.getById(bookingOnTheFlyRequestDto.getChargingStationId());

            BookingDto newBookingDto = new BookingDto(
                    null,
                    "",
                    bookingOnTheFlyRequestDto.getCustomerId(),
                    bookingOnTheFlyRequestDto.getChargingStationId(),
                    bookingOnTheFlyRequestDto.getChargingPointId(),
                    bookingOnTheFlyRequestDto.getSocketId(),
                    chargingStationManager.getSocket(chargingStation, bookingOnTheFlyRequestDto.getSocketId()).getType(),
                    new TimeframeDto(OffsetDateTime.now()),
                    BookingTypeDto.ON_THE_FLY
            );
            Booking newBooking = bookingManager.createNewAndUpdate(newBookingDto);

            applicationEventPublisher.publishEvent(new BookingUpdatedEvent(
                    BookingMapper.buildBookingKafkaDto(newBooking)
            ));
            return BookingMapper.buildBookingDto(newBooking);
        }else{
            throw new NoAvailableSocketException("The socket you have selected is not available. Please select another one");
        }
    }

}
