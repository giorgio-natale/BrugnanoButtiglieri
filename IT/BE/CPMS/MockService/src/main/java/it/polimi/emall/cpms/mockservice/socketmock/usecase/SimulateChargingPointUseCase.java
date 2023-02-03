package it.polimi.emall.cpms.mockservice.socketmock.usecase;

import it.polimi.emall.cpms.mockservice.generated.http.client.cpms_chargingmanagementservice.endpoints.ChargingManagementApi;
import it.polimi.emall.cpms.mockservice.generated.http.client.cpms_chargingmanagementservice.model.SocketStatusClientDto;
import it.polimi.emall.cpms.mockservice.generated.http.server.model.SocketStatusDto;
import it.polimi.emall.cpms.mockservice.socketmock.model.SocketMock;
import it.polimi.emall.cpms.mockservice.socketmock.model.SocketMockManager;
import it.polimi.emall.cpms.mockservice.socketmock.model.dto.SocketDeliveryInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@Slf4j
public class SimulateChargingPointUseCase {
    private final SocketMockManager socketMockManager;
    private final ChargingManagementApi chargingManagementApi;

    private final int waitInReadyStateSeconds;

    public SimulateChargingPointUseCase(
            SocketMockManager socketMockManager,
            ChargingManagementApi chargingManagementApi,
            @Value("${socket-mock.wait-in-ready-state-seconds}") int waitInReadyStateSeconds
    ) {
        this.socketMockManager = socketMockManager;
        this.chargingManagementApi = chargingManagementApi;
        this.waitInReadyStateSeconds = waitInReadyStateSeconds;
    }

    @Transactional
    public void simulateChargingPoints(){
        socketMockManager.getActiveSocketMocks().forEach(socketMock -> {
            OffsetDateTime now = OffsetDateTime.now();
            try {
                SocketStatusClientDto socketStatusClientDto = socketMock.getUpdatedSocketStatusDto(
                        now,
                        waitInReadyStateSeconds
                );
                log.info("Sending to ChargingManagement request to change socket status of {} to {}", socketMock.getSocketId(), socketStatusClientDto.getStatus());
                if(!socketStatusClientDto.getStatus().equals(SocketStatusDto.SOCKETREADYSTATUS.getValue())) {
                    chargingManagementApi.putSocketStatus(
                            socketMock.getChargingStationId(),
                            socketMock.getChargingPointId(),
                            socketMock.getSocketId(),
                            socketStatusClientDto
                    ).block();
                }
            }catch (RuntimeException e){
                log.error("Error while simulating socket {}", socketMock.getSocketId());
            }
        });
    }

    @Transactional
    public void updateChargingPointStatus(
            Long socketId,
            SocketStatusDto status
    ){
        SocketMock socketMock = socketMockManager.getEntityByKey(socketId);
        Integer initialKwHLeftToCharge = null;
        if(status.equals(SocketStatusDto.SOCKETDELIVERINGSTATUS))
            initialKwHLeftToCharge = 30;
        socketMockManager.setStatus(socketMock, new SocketDeliveryInfo(
                status,
                initialKwHLeftToCharge
        ));
    }
}
