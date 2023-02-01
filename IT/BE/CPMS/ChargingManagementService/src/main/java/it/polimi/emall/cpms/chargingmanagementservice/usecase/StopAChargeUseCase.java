package it.polimi.emall.cpms.chargingmanagementservice.usecase;

import it.polimi.emall.cpms.chargingmanagementservice.generated.http.client.cpms_mockingservice.endpoints.ChargingPointMockApi;
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
    private final ChargingPointMockApi chargingPointMockApi;

    public StopAChargeUseCase(
            BookingManager bookingManager, SocketCurrentStatusManager socketCurrentStatusManager,
            ApplicationEventPublisher applicationEventPublisher, PlatformTransactionManager transactionManager, ChargingPointMockApi chargingPointMockApi) {
        super(bookingManager, socketCurrentStatusManager, applicationEventPublisher, transactionManager);
        this.chargingPointMockApi = chargingPointMockApi;
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

        chargingPointMockApi.putChargingPointMock(
                chargingStationId,
                chargingPointId,
                socketId,
                SocketStatusEnum.SocketStoppedStatus.name()
        ).block();

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

        chargingPointMockApi.putChargingPointMock(
                chargingStationId,
                chargingPointId,
                socketId,
                SocketStatusEnum.SocketAvailableStatus.name()
        ).block();
    }





}
