package it.polimi.emall.cpms.chargingmanagementservice.mapper;

import it.polimi.emall.cpms.chargingmanagementservice.model.booking.Booking;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.ProgressInformation;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketStatusEnum;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.dto.SocketStatusUpdateDto;

public class SocketUpdateMapper {
    public static SocketStatusUpdateDto buildSocketStatusUpdateDto(
            Booking booking, SocketStatusEnum status,
            ProgressInformation progressInformation)
    {
        return new SocketStatusUpdateDto(
                booking.getSocketId(),
                booking.getChargingPointId(),
                booking.getChargingStationId(),
                booking.getId(),
                status,
                progressInformation
        );
    }
}
