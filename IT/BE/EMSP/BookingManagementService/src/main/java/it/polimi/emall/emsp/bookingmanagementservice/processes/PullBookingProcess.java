package it.polimi.emall.emsp.bookingmanagementservice.processes;

import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.endpoints.BookingApi;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingStatusClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.mappers.BookingDtoMapper;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.Booking;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingStatusEnum;
import it.polimi.emall.emsp.bookingmanagementservice.model.customerdevice.DeviceManager;
import it.polimi.emall.emsp.bookingmanagementservice.usecases.NotificationDataDto;
import it.polimi.emall.emsp.bookingmanagementservice.usecases.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PullBookingProcess {
    private final BookingApi bookingApi;
    private final BookingManager bookingManager;

    private final TransactionTemplate transactionTemplate;

    private final WebClient.RequestBodySpec notificationWebRequestBodySpec;

    private final DeviceManager deviceManager;


    public PullBookingProcess(
            BookingApi bookingApi, BookingManager bookingManager,
            PlatformTransactionManager transactionManager,
            WebClient.RequestBodySpec notificationWebRequestBodySpec, DeviceManager deviceManager) {
        this.bookingApi = bookingApi;
        this.bookingManager = bookingManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.notificationWebRequestBodySpec = notificationWebRequestBodySpec;
        this.deviceManager = deviceManager;
    }
    @EventListener(ApplicationReadyEvent.class)
    public void startup(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    updateBookings();
                }catch(RuntimeException e){
                    log.error("Cannot update booking {}", e.getMessage());
                }
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

        var notifications = transactionTemplate.execute(status -> {
            Set<NotificationDto> notificationsToSend = new HashSet<>();
            bookingIdByChargingStationId.forEach(bookingId -> {
                try {
                    BookingClientDto bookingClientDto = bookingClientDtoById.get(bookingId);
                    BookingStatusClientDto bookingStatusClientDto = bookingStatusClientDtoById.get(bookingId);

                    if (bookingClientDto == null || bookingStatusClientDto == null) {
                        try {
                            bookingManager.delete(bookingId);
                            return;
                        } catch (RuntimeException e) {
                            log.error("Error while pulling booking id {}", bookingId);
                            return;
                        }
                    }

                    bookingManager.getByIdOpt(bookingId).ifPresent(booking -> {
                        if (!booking.getBookingStatus().getBookingStatus().equals(BookingStatusEnum.BookingStatusCompleted)
                                && bookingStatusClientDto.getBookingStatus().equals(BookingStatusEnum.BookingStatusCompleted.name())
                        ) {
                            deviceManager.getByIdOpt(booking.getCustomerId()).ifPresent(device -> {
                                notificationsToSend.add(new NotificationDto(
                                        device.getExpoToken(),
                                        new NotificationDataDto(bookingId)));
                            });

                        }
                    });
                    Booking booking = bookingManager.getOrCreateNewAndUpdate(bookingId, BookingDtoMapper.buildBookingDto(bookingClientDto));
                    bookingManager.updateStatus(booking, BookingDtoMapper.buildBookingStatusDto(bookingStatusClientDto));
                }catch (RuntimeException e){
                    log.error("Failed to pull booking id {}: {}", bookingId, e.getMessage());
                }
            });
            return notificationsToSend;
        });
        assert notifications != null;
        notifications.forEach(notificationDto -> {
            log.info("Sending notification for booking id {}", notificationDto.data.bookingId);
            notificationWebRequestBodySpec
                    .bodyValue(notificationDto)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe();
        });
    }
}
