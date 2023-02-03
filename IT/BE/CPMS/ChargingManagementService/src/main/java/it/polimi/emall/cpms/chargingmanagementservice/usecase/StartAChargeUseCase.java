package it.polimi.emall.cpms.chargingmanagementservice.usecase;

import it.polimi.emall.cpms.chargingmanagementservice.generated.http.client.cpms_mockingservice.endpoints.ChargingPointMockApi;
import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.model.SocketDeliveringStatusDto;
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

@Service
public class StartAChargeUseCase extends ChargeUseCase{

    private final Logger logger = LoggerFactory.getLogger(StartAChargeUseCase.class);
    private final ChargingPointMockApi chargingPointMockApi;

    public StartAChargeUseCase(
            BookingManager bookingManager,
            SocketCurrentStatusManager socketCurrentStatusManager,
            PlatformTransactionManager platformTransactionManager,
            ApplicationEventPublisher applicationEventPublisher, ChargingPointMockApi chargingPointMockApi) {
        super(bookingManager, socketCurrentStatusManager, applicationEventPublisher, platformTransactionManager);

        this.chargingPointMockApi = chargingPointMockApi;
    }

    public void makeSocketReady(Long chargingStationId, Long chargingPointId, Long socketId){
        SocketCurrentStatus socketCurrentStatus = getSocketCurrentStatus(chargingStationId, chargingPointId, socketId);

        Booking booking = bookingManager.getCurrentBookingByTime(chargingStationId, chargingPointId, socketId);
        updateSocketStatusAndSendEvent(socketId, new ProgressInformation(-1, 0.0), booking, SocketStatusEnum.SocketReadyStatus);


        chargingPointMockApi.putChargingPointMock(
                chargingStationId,
                chargingPointId,
                socketId,
                SocketStatusEnum.SocketReadyStatus.name()
        ).block();

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
                logger.warn(
                        "Charging point status was not in synch: socket {} status: {}, chargingPointStatus: {}." +
                        "Trying to reconcile the charging point status.",
                        socketId,
                        socketCurrentStatus.getSocketStatusEnum(),
                        SocketStatusEnum.SocketReadyStatus
                );
                chargingPointMockApi.putChargingPointMock(
                        chargingStationId,
                        chargingPointId,
                        socketId,
                        SocketStatusEnum.SocketAvailableStatus.name()
                ).block();
                logger.info(
                        "Charging point status reconciled"
                );
            }else{
                throw e;
            }
        }


        chargingPointMockApi.putChargingPointMock(
                chargingStationId,
                chargingPointId,
                socketId,
                SocketStatusEnum.SocketDeliveringStatus.name()
        ).block();


    }
}
