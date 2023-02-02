package it.polimi.emall.cpms.chargingmanagementservice.controller;

import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.controller.ChargingStationApi;
import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.model.*;
import it.polimi.emall.cpms.chargingmanagementservice.usecase.ShowChargingStationStatusUseCase;
import it.polimi.emall.cpms.chargingmanagementservice.usecase.StartAChargeUseCase;
import it.polimi.emall.cpms.chargingmanagementservice.usecase.StopAChargeUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChargingManagementController implements ChargingStationApi {

    private final StartAChargeUseCase startAChargeUseCase;
    private final StopAChargeUseCase stopAChargeUseCase;

    private final ShowChargingStationStatusUseCase showChargingStationStatusUseCase;

    public ChargingManagementController(StartAChargeUseCase startAChargeUseCase, StopAChargeUseCase stopAChargeUseCase, ShowChargingStationStatusUseCase showChargingStationStatusUseCase) {
        this.startAChargeUseCase = startAChargeUseCase;
        this.stopAChargeUseCase = stopAChargeUseCase;
        this.showChargingStationStatusUseCase = showChargingStationStatusUseCase;
    }

    @Override
    public ResponseEntity<SocketStatusDto> getSocketStatus(Long chargingStationId, Long chargingPointId, Long socketId) {
        return new ResponseEntity<>(showChargingStationStatusUseCase.getSocketStatus(socketId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> putSocketStatus(Long chargingStationId, Long chargingPointId, Long socketId, SocketStatusDto socketStatusDto) {
        if(socketStatusDto instanceof SocketReadyStatusDto){
            startAChargeUseCase.makeSocketReady(chargingStationId, chargingPointId, socketId);
            return new ResponseEntity<>(HttpStatus.OK);
        }else if(socketStatusDto instanceof SocketDeliveringStatusDto){
            startAChargeUseCase.makeSocketDelivering(chargingStationId, chargingPointId, socketId, ((SocketDeliveringStatusDto) socketStatusDto));  //TODO: check that the messages comes from chargingPoint
            return new ResponseEntity<>(HttpStatus.OK);
        }else if(socketStatusDto instanceof SocketStoppedStatusDto){
            stopAChargeUseCase.stopEnergyDelivering(chargingStationId, chargingPointId, socketId);  //TODO: check that the messages comes from chargingPoint
            return new ResponseEntity<>(HttpStatus.OK);
        }else if(socketStatusDto instanceof SocketAvailableStatusDto){
            stopAChargeUseCase.makeSocketAvailable(chargingStationId, chargingPointId, socketId);  //TODO: check that the messages comes from chargingPoint
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @Override
    public ResponseEntity<ChargingStationStatusDto> getChargingStationStatus(Long chargingStationId) {
        return new ResponseEntity<>(
                showChargingStationStatusUseCase.getChargingStationStatus(chargingStationId),
                HttpStatus.OK
        );
    }
}
