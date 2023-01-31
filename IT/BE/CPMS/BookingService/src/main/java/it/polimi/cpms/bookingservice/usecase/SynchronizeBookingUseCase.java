package it.polimi.cpms.bookingservice.usecase;

import it.polimi.cpms.bookingservice.mappers.BookingMapper;
import it.polimi.cpms.bookingservice.model.booking.Booking;
import it.polimi.cpms.bookingservice.model.booking.BookingManager;
import it.polimi.cpms.bookingservice.model.booking.BookingStatus;
import it.polimi.cpms.bookingservice.model.booking.dto.SocketStatusUpdateDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SynchronizeBookingUseCase {
    private final BookingManager bookingManager;

    public SynchronizeBookingUseCase(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

    public BookingDto getBookingDto(Long bookingId){
        return BookingMapper.buildBookingDto(bookingManager.getEntityByKey(bookingId));
    }

    @Transactional
    public BookingStatusDto getBookingStatusDto(Long bookingId){
        return BookingMapper.buildBookingStatusDto(bookingManager.getEntityByKey(bookingId).getBookingStatus());
    }

    public List<BookingDto> getAllBookings(){
        return bookingManager.getAll()
                .stream()
                .sorted(Comparator.comparingLong(Booking::getId))
                .map(BookingMapper::buildBookingDto)
                .collect(Collectors.toList());
    }

    public List<BookingStatusDto> getAllBookingStatuses(){
        return bookingManager.getAllBookingStatuses()
                .stream()
                .sorted(Comparator.comparingLong(BookingStatus::getId))
                .map(BookingMapper::buildBookingStatusDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateInProgressInformationForBooking(SocketStatusUpdateDto socketStatusUpdateDto){
        bookingManager.updateStatus(
                bookingManager.getEntityByKey(socketStatusUpdateDto.bookingId),
                BookingMapper.buildBookingStatusDto(socketStatusUpdateDto)
        );
    }

    @Transactional
    public void closeBooking(Long bookingId){
        Booking booking = bookingManager.getEntityByKey(bookingId);
        if(booking.getBookingType().equals(BookingTypeDto.ON_THE_FLY)){
            BookingDto bookingDto = BookingMapper.buildBookingDto(booking);
            bookingDto.timeframe(
                    new TimeframeDto(booking.getTimeFrame().getStartInstant()).endInstant(OffsetDateTime.now())
            );
        }
        bookingManager.updateStatus(booking, new BookingStatusCompletedDto());
    }
}
