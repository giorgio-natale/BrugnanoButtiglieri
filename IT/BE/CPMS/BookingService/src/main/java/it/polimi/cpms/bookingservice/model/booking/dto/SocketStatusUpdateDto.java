package it.polimi.cpms.bookingservice.model.booking.dto;

import it.polimi.cpms.bookingservice.model.booking.ProgressInformation;

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
