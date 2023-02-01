package it.polimi.emall.cpms.mockservice.socketmock.process;

import it.polimi.emall.cpms.mockservice.controller.ChargingStationController;
import it.polimi.emall.cpms.mockservice.socketmock.model.SocketMockManager;
import it.polimi.emall.cpms.mockservice.socketmock.model.dto.SocketConfigurationDto;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public class StartupProcess {
    private final SocketMockManager socketMockManager;
    private final ChargingStationController chargingStationController;

    public StartupProcess(SocketMockManager socketMockManager, ChargingStationController chargingStationController) {
        this.socketMockManager = socketMockManager;
        this.chargingStationController = chargingStationController;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void startup(){
        Objects.requireNonNull(chargingStationController.getChargingStationConfigurationList().getBody()).forEach(chargingStationDto -> {
            chargingStationDto.getChargingPointList().forEach(chargingPointDto ->
                    chargingPointDto.getSocketList().forEach(socketDto ->
                            socketMockManager.getOrCreateNewAndUpdate(
                                    socketDto.getSocketId(),
                                    new SocketConfigurationDto(
                                            socketDto.getSocketId(),
                                            chargingPointDto.getChargingPointId(),
                                            chargingStationDto.getChargingStationId(),
                                            socketDto.getType()
                                    )
                            )
                    )
            );
        });
    }
}
