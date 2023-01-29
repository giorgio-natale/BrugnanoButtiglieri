package it.polimi.emall.cpms.chargingmanagementservice.controller;

import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.controller.ChargingStationApi;
import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.model.SocketStatusDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChargingManagementController implements ChargingStationApi {
    @Override
    public ResponseEntity<SocketStatusDto> getSocketStatus(Long chargingStationId, String chargingPointId, String socketId) {
        return ChargingStationApi.super.getSocketStatus(chargingStationId, chargingPointId, socketId);
    }

    @Override
    public ResponseEntity<Void> putSocketStatus(Long chargingStationId, String chargingPointId, String socketId, SocketStatusDto socketStatusDto) {
        return ChargingStationApi.super.putSocketStatus(chargingStationId, chargingPointId, socketId, socketStatusDto);
    }
}
