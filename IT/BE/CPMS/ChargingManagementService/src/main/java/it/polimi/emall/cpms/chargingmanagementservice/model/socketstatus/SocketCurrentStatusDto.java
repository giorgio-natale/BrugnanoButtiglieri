package it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus;

public class SocketCurrentStatusDto {
    public final Long id;
    public final Long chargingPointId;
    public final Long chargingStationId;
    public final SocketStatusEnum socketStatusEnum;
    public final ProgressInformation progressInformation;

    public SocketCurrentStatusDto(
            Long id,Long chargingPointId, Long chargingStationId,
            SocketStatusEnum socketStatusEnum, ProgressInformation progressInformation
    ) {
        this.id = id;
        this.chargingPointId = chargingPointId;
        this.chargingStationId = chargingStationId;
        this.socketStatusEnum = socketStatusEnum;
        this.progressInformation = progressInformation;
    }
}
