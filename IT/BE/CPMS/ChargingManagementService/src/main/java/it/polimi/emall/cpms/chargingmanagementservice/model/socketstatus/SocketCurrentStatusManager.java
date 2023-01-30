package it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus;

import it.polimi.emall.cpms.chargingmanagementservice.utils.IdAssignedManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class SocketCurrentStatusManager extends IdAssignedManager<SocketCurrentStatus, Long, SocketCurrentStatusDto> {

    protected SocketCurrentStatusManager(CrudRepository<SocketCurrentStatus, Long> crudRepository) {
        super(crudRepository);
    }

    public SocketCurrentStatus updateStatus(SocketCurrentStatus socketCurrentStatus, SocketStatusEnum status, ProgressInformation progressInformation){
        socketCurrentStatus.updateStatus(status, progressInformation);
        return socketCurrentStatus;
    }

    @Override
    protected SocketCurrentStatus updateEntity(SocketCurrentStatus currentState, SocketCurrentStatusDto desiredState) {
        currentState.updateSocketInfo(desiredState.chargingPointId, desiredState.chargingStationId);
        currentState.updateStatus(desiredState.socketStatusEnum, desiredState.progressInformation);
        return currentState;
    }

    @Override
    protected SocketCurrentStatus createDefault(Long key) {
        return new SocketCurrentStatus(key);
    }
}
