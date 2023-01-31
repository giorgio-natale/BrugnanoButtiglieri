package it.polimi.emall.cpms.chargingmanagementservice.usecase;

import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.model.SocketDeliveringStatusDto;
import it.polimi.emall.cpms.chargingmanagementservice.mapper.SocketUpdateMapper;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.Booking;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingStatusEnum;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.ProgressInformation;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatus;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatusManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class StartAChargeUseCase extends ChargeUseCase{

    private final Logger logger = LoggerFactory.getLogger(StartAChargeUseCase.class);

    public StartAChargeUseCase(
            BookingManager bookingManager,
            SocketCurrentStatusManager socketCurrentStatusManager,
            PlatformTransactionManager platformTransactionManager,
            ApplicationEventPublisher applicationEventPublisher) {
        super(bookingManager, socketCurrentStatusManager, applicationEventPublisher, platformTransactionManager);

    }

    public void makeSocketReady(Long chargingStationId, Long chargingPointId, Long socketId){
        SocketCurrentStatus socketCurrentStatus = getSocketCurrentStatus(chargingStationId, chargingPointId, socketId);

        Booking booking = bookingManager.getCurrentBookingByTime(chargingStationId, chargingPointId, socketId);
        updateSocketStatusAndSendEvent(socketId, null, booking, SocketStatusEnum.SocketReadyStatus);


        //TODO: send ready command to charging point

    }

    public void makeSocketDelivering(Long chargingStationId, Long chargingPointId, Long socketId, SocketDeliveringStatusDto socketDeliveringStatusDto){
        SocketCurrentStatus socketCurrentStatus = getSocketCurrentStatus(chargingStationId, chargingPointId, socketId);


        Booking currentBooking = bookingManager
                .getCurrentBookingByBookingStatus(
                        chargingStationId,
                        chargingPointId,
                        socketId,
                        BookingStatusEnum.BookingStatusInProgress
                );

        try{
            updateSocketStatusAndSendEvent(
                    socketId,
                    new ProgressInformation(socketDeliveringStatusDto.getExpectedMinutesLeft(), socketDeliveringStatusDto.getkWhAbsorbed()),
                    currentBooking,
                    SocketStatusEnum.SocketDeliveringStatus
            );

        }catch (IllegalStateException e){
            if(socketCurrentStatus.getSocketStatusEnum().equals(SocketStatusEnum.SocketAvailableStatus)){
                logger.info(
                        "Charging point status was not in synch: socket {} status: {}, chargingPointStatus: {}." +
                        "Trying to reconcile the charging point status.",
                        socketId,
                        socketCurrentStatus.getSocketStatusEnum(),
                        SocketStatusEnum.SocketReadyStatus
                );
                //TODO: tell the charging point to go back in the available state
                logger.info(
                        "Charging point status reconciled"
                );
            }else{
                throw e;
            }
        }

        //TODO: send delivery command to charging point


    }
}
