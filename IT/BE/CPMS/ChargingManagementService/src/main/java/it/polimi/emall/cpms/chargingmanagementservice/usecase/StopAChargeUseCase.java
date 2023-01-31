package it.polimi.emall.cpms.chargingmanagementservice.usecase;

import it.polimi.emall.cpms.chargingmanagementservice.model.booking.Booking;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingStatusEnum;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatusManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class StopAChargeUseCase extends ChargeUseCase{

    private final Logger logger = LoggerFactory.getLogger(StartAChargeUseCase.class);

    public StopAChargeUseCase(
            BookingManager bookingManager, SocketCurrentStatusManager socketCurrentStatusManager,
            ApplicationEventPublisher applicationEventPublisher, PlatformTransactionManager transactionManager) {
        super(bookingManager, socketCurrentStatusManager, applicationEventPublisher, transactionManager);
    }

    public void stopEnergyDelivering(Long chargingStationId, Long chargingPointId, Long socketId){

        getSocketCurrentStatus(chargingStationId, chargingPointId, socketId);
        Booking currentBooking = bookingManager.getCurrentBookingByBookingStatus(
                chargingStationId,
                chargingPointId,
                socketId,
                BookingStatusEnum.BookingStatusInProgress
        );

        updateSocketStatusAndSendEvent(
                socketId,
                null,
                currentBooking,
                SocketStatusEnum.SocketStoppedStatus
        );

        //TODO: send stop command to charging point

    }

    public void makeSocketAvailable(Long chargingStationId, Long chargingPointId, Long socketId){
        getSocketCurrentStatus(chargingStationId, chargingPointId, socketId);

        transactionTemplate.execute(status -> {
            socketCurrentStatusManager.updateStatus(
                    socketCurrentStatusManager.getEntityByKey(socketId),
                    SocketStatusEnum.SocketAvailableStatus,
                    null
            );
            return null;

        });

        //TODO: send available command to charging point
    }





}
