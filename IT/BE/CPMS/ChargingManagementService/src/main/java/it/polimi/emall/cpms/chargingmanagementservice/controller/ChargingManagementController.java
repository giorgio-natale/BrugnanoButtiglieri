package it.polimi.emall.cpms.chargingmanagementservice.controller;

import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.controller.ChargingStationApi;
import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.model.SocketReadyStatusDto;
import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.model.SocketStatusDto;
import it.polimi.emall.cpms.chargingmanagementservice.usecase.StartAChargeUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChargingManagementController implements ChargingStationApi {

    private final StartAChargeUseCase startAChargeUseCase;

    public ChargingManagementController(StartAChargeUseCase startAChargeUseCase) {
        this.startAChargeUseCase = startAChargeUseCase;
    }

    @Override
    public ResponseEntity<SocketStatusDto> getSocketStatus(Long chargingStationId, Long chargingPointId, Long socketId) {
        return ChargingStationApi.super.getSocketStatus(chargingStationId, chargingPointId, socketId);
    }

    @Override
    public ResponseEntity<Void> putSocketStatus(Long chargingStationId, Long chargingPointId, Long socketId, SocketStatusDto socketStatusDto) {
        if(socketStatusDto instanceof SocketReadyStatusDto){
            startAChargeUseCase.makeSocketReady(chargingStationId, chargingPointId, socketId);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }
}
