package it.polimi.emall.cpms.chargingmanagementservice.usecase;

import it.polimi.emall.cpms.chargingmanagementservice.mapper.SocketUpdateMapper;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.Booking;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingStatusEnum;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.ProgressInformation;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatus;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatusManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketStatusEnum;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.NoSuchElementException;
import java.util.Objects;

public abstract class ChargeUseCase {
    protected final BookingManager bookingManager;
    protected final SocketCurrentStatusManager socketCurrentStatusManager;

    protected final TransactionTemplate transactionTemplate;

    protected final ApplicationEventPublisher applicationEventPublisher;


    protected ChargeUseCase(
            BookingManager bookingManager,
            SocketCurrentStatusManager socketCurrentStatusManager,
            ApplicationEventPublisher applicationEventPublisher,
            PlatformTransactionManager transactionManager
    ) {
        this.bookingManager = bookingManager;
        this.socketCurrentStatusManager = socketCurrentStatusManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    protected SocketCurrentStatus getSocketCurrentStatus(Long chargingStationId, Long chargingPointId, Long socketId){
        SocketCurrentStatus socketCurrentStatus = socketCurrentStatusManager.getEntityByKey(socketId);
        if(!Objects.equals(socketCurrentStatus.getChargingPointId(), chargingPointId)
                || !Objects.equals(socketCurrentStatus.getChargingStationId(), chargingStationId))
            throw new NoSuchElementException(String.format(
                    "Cannot find the socket %d/%d/%d",
                    chargingStationId,
                    chargingPointId,
                    socketId
            ));

        return socketCurrentStatus;
    }

    protected void updateSocketStatusAndSendEvent(
            Long socketId,
            ProgressInformation progressInformation,
            Booking currentBooking,
            SocketStatusEnum socketStatus
    ){
        transactionTemplate.execute(status -> {
            socketCurrentStatusManager.updateStatus(
                    socketCurrentStatusManager.getEntityByKey(socketId),
                    socketStatus,
                    progressInformation
            );
            applicationEventPublisher.publishEvent(SocketUpdateMapper.buildSocketStatusUpdateDto(
                    currentBooking,
                    socketStatus,
                    progressInformation
            ));

            return null;

        });
    }
}
