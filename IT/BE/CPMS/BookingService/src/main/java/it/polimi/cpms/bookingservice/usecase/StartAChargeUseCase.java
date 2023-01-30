package it.polimi.cpms.bookingservice.usecase;

import it.polimi.cpms.bookingservice.mappers.BookingMapper;
import it.polimi.cpms.bookingservice.model.booking.Booking;
import it.polimi.cpms.bookingservice.model.booking.BookingManager;
import it.polimi.cpms.bookingservice.model.booking.BookingStatusEnum;
import it.polimi.cpms.bookingservice.model.booking.dto.BookingKafkaDto;
import it.polimi.cpms.bookingservice.model.booking.event.BookingUpdatedEvent;
import it.polimi.emall.cpms.bookingservice.generated.http.client.chargingmanagementservice.endpoints.ChargingManagementApi;
import it.polimi.emall.cpms.bookingservice.generated.http.client.chargingmanagementservice.model.SocketStatusClientDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingStatusInProgressDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.OffsetDateTime;

@Service
public class StartAChargeUseCase {

    private final BookingManager bookingManager;
    private final ChargingManagementApi chargingManagementApi;

    private final TransactionTemplate transactionTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    public StartAChargeUseCase(
            BookingManager bookingManager,
            ChargingManagementApi chargingManagementApi,
            PlatformTransactionManager platformTransactionManager,
            ApplicationEventPublisher applicationEventPublisher){
        this.bookingManager = bookingManager;
        this.chargingManagementApi = chargingManagementApi;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void startACharge(Long bookingId){
        Booking booking = bookingManager.getEntityByKey(bookingId);
        if(!booking.getBookingStatus().getBookingStatus().equals(BookingStatusEnum.BookingStatusPlanned))
            throw new IllegalStateException(String.format("Booking with code %s is not planned", booking.getBookingCode()));
        OffsetDateTime now = OffsetDateTime.now();
        if(!booking.getTimeFrame().contains(now))
            throw new IllegalStateException(String.format(
                    "Booking with code %s cannot be started now. Interval: %s (now: %s)",
                    booking.getBookingCode(),
                    booking.getTimeFrame(),
                    now
            ));

        chargingManagementApi.putSocketStatus(
                booking.getChargingStationId(),
                booking.getChargingPointId(),
                booking.getSocketId(),
                new SocketStatusClientDto().status("SocketReadyStatus")
        ).block();

        BookingKafkaDto updatedBookingKafkaDto = transactionTemplate.execute(status -> {
            Booking updatedBooking = bookingManager.updateStatus(
                    bookingManager.getEntityByKey(bookingId),
                    new BookingStatusInProgressDto()
            );
            return BookingMapper.buildBookingKafkaDto(updatedBooking);
        });

        applicationEventPublisher.publishEvent(new BookingUpdatedEvent(updatedBookingKafkaDto));

    }
}
