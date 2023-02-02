package it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus;

import it.polimi.emall.cpms.chargingmanagementservice.utils.IdAssignedManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SocketCurrentStatusManager extends IdAssignedManager<SocketCurrentStatus, Long, SocketCurrentStatusDto> {

    private final SocketCurrentStatusRepository socketCurrentStatusRepository;
    protected SocketCurrentStatusManager(SocketCurrentStatusRepository socketCurrentStatusRepository) {
        super(socketCurrentStatusRepository);
        this.socketCurrentStatusRepository = socketCurrentStatusRepository;
    }

    public SocketCurrentStatus updateStatus(SocketCurrentStatus socketCurrentStatus, SocketStatusEnum status, ProgressInformation progressInformation){
        socketCurrentStatus.updateStatus(status, progressInformation);
        return socketCurrentStatus;
    }

    public SocketCurrentStatus updateSocketInfo(SocketCurrentStatus socketCurrentStatus, SocketCurrentStatusDto desiredState){
        socketCurrentStatus.updateSocketInfo(
                desiredState.socketCode,
                desiredState.chargingPointId,
                desiredState.chargingPointCode,
                desiredState.chargingPointMode,
                desiredState.chargingStationId,
                desiredState.socketType
        );
        return socketCurrentStatus;
    }

    public Set<SocketCurrentStatus> getAllSocketStatusesOfChargingStation(Long chargingStationId){
        return socketCurrentStatusRepository.findAllByChargingStationId(chargingStationId);
    }

    @Override
    protected SocketCurrentStatus updateEntity(SocketCurrentStatus currentState, SocketCurrentStatusDto desiredState) {
        currentState.updateSocketInfo(
                desiredState.socketCode,
                desiredState.chargingPointId,
                desiredState.chargingPointCode,
                desiredState.chargingPointMode,
                desiredState.chargingStationId,
                desiredState.socketType
        );
        currentState.updateStatus(desiredState.socketStatusEnum, desiredState.progressInformation);
        return currentState;
    }

    @Override
    protected SocketCurrentStatus createDefault(Long key) {
        return new SocketCurrentStatus(key);
    }
}
