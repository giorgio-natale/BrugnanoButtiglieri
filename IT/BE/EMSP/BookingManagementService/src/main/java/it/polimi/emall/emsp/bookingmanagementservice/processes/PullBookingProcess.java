package it.polimi.emall.emsp.bookingmanagementservice.processes;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.endpoints.BookingApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingStatusClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.mappers.BookingDtoMapper;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.Booking;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PullBookingProcess {
    private final BookingApi bookingApi;
    private final BookingManager bookingManager;

    private final TransactionTemplate transactionTemplate;


    public PullBookingProcess(
            BookingApi bookingApi, BookingManager bookingManager,
            PlatformTransactionManager transactionManager
    ) {
        this.bookingApi = bookingApi;
        this.bookingManager = bookingManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }
    @EventListener(ApplicationReadyEvent.class)
    public void startup(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateBookings();
            }
        }, 0, 20000);
    }

    public void updateBookings(){
        Set<Booking> bookings = bookingManager.getAll();
        Set<Long> bookingIdByChargingStationId = bookings
                .stream()
                .map(Booking::getId)
                .collect(Collectors.toSet());

        Map<Long, BookingClientDto> bookingClientDtoById =
                Objects.requireNonNull(bookingApi.getAllBookings().collectList().block())
                        .stream()
                        .collect(Collectors.toMap(
                                BookingClientDto::getBookingId,
                                bookingClientDto -> bookingClientDto
                        ));
        Map<Long, BookingStatusClientDto> bookingStatusClientDtoById =
                Objects.requireNonNull(bookingApi.getAllStatuses().collectList().block())
                        .stream()
                        .collect(Collectors.toMap(
                                BookingStatusClientDto::getBookingId,
                                bookingStatusClientDto -> bookingStatusClientDto
                        ));

        transactionTemplate.execute(status -> {
            bookingIdByChargingStationId.forEach(bookingId -> {
                BookingClientDto bookingClientDto = bookingClientDtoById.get(bookingId);
                BookingStatusClientDto bookingStatusClientDto = bookingStatusClientDtoById.get(bookingId);

                Booking booking = bookingManager.getOrCreateNewAndUpdate(bookingId, BookingDtoMapper.buildBookingDto(bookingClientDto));
                bookingManager.updateStatus(booking, BookingDtoMapper.buildBookingStatusDto(bookingStatusClientDto));
            });
            return null;
        });
    }
}
