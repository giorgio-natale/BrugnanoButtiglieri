package it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus;

import it.polimi.emall.cpms.chargingmanagementservice.generated.http.client.cpms_mockingservice.model.ChargingPointModeClientDto;
import it.polimi.emall.cpms.chargingmanagementservice.generated.http.client.cpms_mockingservice.model.SocketTypeClientDto;

public class SocketCurrentStatusDto {
    public final Long id;
    public final String socketCode;
    public final Long chargingPointId;
    public final String chargingPointCode;
    public final ChargingPointModeClientDto chargingPointMode;

    public final Long chargingStationId;

    public final SocketTypeClientDto socketType;
    public final SocketStatusEnum socketStatusEnum;
    public final ProgressInformation progressInformation;

    public SocketCurrentStatusDto(
            Long id, String socketCode, Long chargingPointId, String chargingPointCode, ChargingPointModeClientDto chargingPointMode, Long chargingStationId,
            SocketTypeClientDto socketType, SocketStatusEnum socketStatusEnum, ProgressInformation progressInformation
    ) {
        this.id = id;
        this.socketCode = socketCode;
        this.chargingPointId = chargingPointId;
        this.chargingPointCode = chargingPointCode;
        this.chargingPointMode = chargingPointMode;
        this.chargingStationId = chargingStationId;
        this.socketType = socketType;
        this.socketStatusEnum = socketStatusEnum;
        this.progressInformation = progressInformation;
    }
}
