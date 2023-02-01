package it.polimi.emall.cpms.mockservice.socketmock.model;

import it.polimi.emall.cpms.mockservice.generated.http.server.model.SocketStatusDto;
import it.polimi.emall.cpms.mockservice.socketmock.model.dto.SocketConfigurationDto;
import it.polimi.emall.cpms.mockservice.socketmock.model.dto.SocketDeliveryInfo;
import it.polimi.emall.cpms.mockservice.utils.IdAssignedManager;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SocketMockManager extends IdAssignedManager<SocketMock, Long, SocketConfigurationDto> {
    private final SocketMockRepository socketMockRepository;

    protected SocketMockManager(SocketMockRepository crudRepository) {
        super(crudRepository);
        this.socketMockRepository = crudRepository;
    }

    public void setStatus(SocketMock socketMock, SocketDeliveryInfo socketDeliveryInfo){
        socketMock.setStatus(socketDeliveryInfo);
    }

    public Set<SocketMock> getActiveSocketMocks(){
        return socketMockRepository.findSocketMocksBySocketStatusNot(SocketStatusDto.SOCKETAVAILABLESTATUS);
    }


    @Override
    protected SocketMock updateEntity(SocketMock currentState, SocketConfigurationDto desiredState) {
        currentState.updateConfiguration(
                desiredState.chargingPointId,
                desiredState.chargingStationId,
                desiredState.socketType
        );
        return currentState;
    }

    @Override
    protected SocketMock createDefault(Long key) {
        return new SocketMock(key);
    }
}
