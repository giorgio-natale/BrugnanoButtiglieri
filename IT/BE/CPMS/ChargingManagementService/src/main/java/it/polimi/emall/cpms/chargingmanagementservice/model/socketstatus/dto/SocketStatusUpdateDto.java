package it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.dto;

import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.ProgressInformation;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketStatusEnum;

public class SocketStatusUpdateDto {
    public final Long socketId;
    public final Long chargingPointId;
    public final Long chargingStationId;
    public final Long bookingId;
    public final SocketStatusEnum socketStatus;
    public final ProgressInformation progressInformation;

    public SocketStatusUpdateDto(
            Long socketId, Long chargingPointId, Long chargingStationId,
            Long bookingId, SocketStatusEnum socketStatus, ProgressInformation progressInformation
    ) {
        this.socketId = socketId;
        this.chargingPointId = chargingPointId;
        this.chargingStationId = chargingStationId;
        this.bookingId = bookingId;
        this.socketStatus = socketStatus;
        this.progressInformation = progressInformation;
    }
}
