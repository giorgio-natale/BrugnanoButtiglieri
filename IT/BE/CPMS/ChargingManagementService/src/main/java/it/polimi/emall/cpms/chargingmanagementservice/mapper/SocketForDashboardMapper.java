package it.polimi.emall.cpms.chargingmanagementservice.mapper;

import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.model.*;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatus;

public class SocketForDashboardMapper {

    public static SocketForDashboardDto buildSocketForDashboardDto(SocketCurrentStatus socketCurrentStatus){
        return new SocketForDashboardDto(
                socketCurrentStatus.getId(),
                socketCurrentStatus.getSocketCode(),
                buildSocketStatusDto(socketCurrentStatus)
        );
    }

    public static SocketStatusDto buildSocketStatusDto(SocketCurrentStatus socketCurrentStatus){
        switch (socketCurrentStatus.getSocketStatusEnum()){
            case SocketDeliveringStatus -> {
                return new SocketDeliveringStatusDto(
                        socketCurrentStatus.getProgressInformation().kWhAbsorbed(),
                        socketCurrentStatus.getProgressInformation().expectedMinutesLeft()
                );
            }
            case SocketReadyStatus -> {
                return new SocketReadyStatusDto();
            }
            case SocketStoppedStatus-> {
                return new SocketStoppedStatusDto();
            }
            case SocketAvailableStatus -> {
                return new SocketAvailableStatusDto();
            }
            default -> throw new IllegalArgumentException(String.format("Socket status %s not supported", socketCurrentStatus.getSocketStatusEnum()));
        }
    }
}
