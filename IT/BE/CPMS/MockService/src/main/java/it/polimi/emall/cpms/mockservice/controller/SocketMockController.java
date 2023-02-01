package it.polimi.emall.cpms.mockservice.controller;

import it.polimi.emall.cpms.mockservice.generated.http.server.controller.ChargingPointMockApi;
import it.polimi.emall.cpms.mockservice.generated.http.server.model.SocketStatusDto;
import it.polimi.emall.cpms.mockservice.socketmock.usecase.SimulateChargingPointUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocketMockController implements ChargingPointMockApi {
    private final SimulateChargingPointUseCase simulateChargingPointUseCase;

    public SocketMockController(SimulateChargingPointUseCase simulateChargingPointUseCase) {
        this.simulateChargingPointUseCase = simulateChargingPointUseCase;
    }

    @Override
    public ResponseEntity<Void> putChargingPointMock(
            Long chargingStationId,
            Long chargingPointId,
            Long socketId,
            String status
    ) {
        simulateChargingPointUseCase.updateChargingPointStatus(socketId, SocketStatusDto.fromValue(status));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
