package it.polimi.emall.cpms.mockservice.socketmock.model.dto;

import it.polimi.emall.cpms.mockservice.generated.http.server.model.SocketTypeDto;

public class SocketConfigurationDto {
    public final Long chargingSocketId;
    public final Long chargingPointId;
    public final Long chargingStationId;
    public final SocketTypeDto socketType;

    public SocketConfigurationDto(
            Long chargingSocketId,
            Long chargingPointId,
            Long chargingStationId,
            SocketTypeDto socketType
    ) {
        this.chargingSocketId = chargingSocketId;
        this.chargingPointId = chargingPointId;
        this.chargingStationId = chargingStationId;
        this.socketType = socketType;
    }
}
