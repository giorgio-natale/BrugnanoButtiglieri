package it.polimi.cpms.bookingservice.processes;

import it.polimi.cpms.bookingservice.mappers.BookingMapper;
import it.polimi.cpms.bookingservice.model.booking.Booking;
import it.polimi.cpms.bookingservice.model.booking.BookingManager;
import it.polimi.cpms.bookingservice.model.booking.BookingStatusEnum;
import it.polimi.cpms.bookingservice.model.booking.dto.SocketStatusEnum;
import it.polimi.cpms.bookingservice.model.booking.event.BookingUpdatedEvent;
import it.polimi.emall.cpms.bookingservice.generated.http.client.chargingmanagementservice.endpoints.ChargingManagementApi;
import it.polimi.emall.cpms.bookingservice.generated.http.client.chargingmanagementservice.model.SocketStatusClientDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingStatusExpiredDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Timer;
import java.util.TimerTask;

@Component
@Slf4j
public class BookingMonitoringProcess {
    private final TransactionTemplate transactionTemplate;
    private final BookingManager bookingManager;

    private final ChargingManagementApi chargingManagementApi;

    private final ApplicationEventPublisher applicationEventPublisher;

    public BookingMonitoringProcess(PlatformTransactionManager transactionManager, BookingManager bookingManager, ChargingManagementApi chargingManagementApi, ApplicationEventPublisher applicationEventPublisher) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.bookingManager = bookingManager;
        this.chargingManagementApi = chargingManagementApi;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startup(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handleExpiringBookings();
            }
        }, 5000, 30000);
    }

    private void handleExpiringBookings(){
        transactionTemplate.execute(status -> {
            bookingManager.getAllExpiringBookings().forEach(booking -> {
                if(booking.getBookingStatus().getBookingStatus().equals(BookingStatusEnum.BookingStatusPlanned)){
                    log.info("Booking {} has expired", booking.getId());
                    booking = bookingManager.updateStatus(booking, new BookingStatusExpiredDto().bookingId(booking.getId()));
                    applicationEventPublisher.publishEvent(new BookingUpdatedEvent(BookingMapper.buildBookingKafkaDto(booking)));
                } else if (booking.getBookingStatus().getBookingStatus().equals(BookingStatusEnum.BookingStatusInProgress)) {
                    log.info("Send request to close booking {} because the timeframe is over", booking.getId());
                    chargingManagementApi.putSocketStatus(
                            booking.getChargingStationId(),
                            booking.getChargingPointId(),
                            booking.getSocketId(),
                            new SocketStatusClientDto()
                                    .status(SocketStatusEnum.SocketStoppedStatus.name())
                    ).block();
                }
            });
            return null;
        });
    }
}
