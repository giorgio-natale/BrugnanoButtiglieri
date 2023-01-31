package it.polimi.cpms.bookingservice.usecase;

import it.polimi.cpms.bookingservice.mappers.BookingMapper;
import it.polimi.cpms.bookingservice.model.booking.Booking;
import it.polimi.cpms.bookingservice.model.booking.BookingManager;
import it.polimi.cpms.bookingservice.model.booking.BookingStatus;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingStatusDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
