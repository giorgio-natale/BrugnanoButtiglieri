package it.polimi.emall.cpms.chargingmanagementservice.usecase;

import it.polimi.emall.cpms.chargingmanagementservice.mapper.SocketUpdateMapper;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.Booking;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatus;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatusManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketStatusEnum;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class StartAChargeUseCase {
    private final BookingManager bookingManager;
    private final SocketCurrentStatusManager socketCurrentStatusManager;

    private final TransactionTemplate transactionTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    public StartAChargeUseCase(
            BookingManager bookingManager,
            SocketCurrentStatusManager socketCurrentStatusManager,
            PlatformTransactionManager platformTransactionManager,
            ApplicationEventPublisher applicationEventPublisher) {
        this.bookingManager = bookingManager;
        this.socketCurrentStatusManager = socketCurrentStatusManager;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void makeSocketReady(Long chargingStationId, Long chargingPointId, Long socketId){
        SocketCurrentStatus socketCurrentStatus = socketCurrentStatusManager.getEntityByKey(socketId);
        if(!Objects.equals(socketCurrentStatus.getChargingPointId(), chargingPointId)
            || !Objects.equals(socketCurrentStatus.getChargingStationId(), chargingStationId))
            throw new NoSuchElementException(String.format(
                    "Cannot find the socket %d/%d/%d",
                    chargingStationId,
                    chargingPointId,
                    socketId
            ));

        //TODO: send ready command to charging point

        transactionTemplate.execute(status -> {
            socketCurrentStatusManager.updateStatus(
                    socketCurrentStatusManager.getEntityByKey(socketId),
                    SocketStatusEnum.SocketReadyStatus,
                    null
            );

            Booking booking = bookingManager.getCurrentBooking(chargingStationId, chargingPointId, socketId);
            applicationEventPublisher.publishEvent(SocketUpdateMapper.buildSocketStatusUpdateDto(
                    booking,
                    SocketStatusEnum.SocketReadyStatus,
                    null
            ));
            return null;
        });

    }
}
