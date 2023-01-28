package it.polimi.cpms.bookingservice.model.chargingstation;

import it.polimi.cpms.bookingservice.utils.IdAssignedManager;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.model.SocketClientDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.SocketTypeDto;
import org.springframework.stereotype.Service;

@Service
class SocketManager extends IdAssignedManager<Socket, Long, SocketClientDto> {

    protected SocketManager(SocketRepository socketRepository) {
        super(socketRepository);
    }

    @Override
    protected Socket updateEntity(Socket currentState, SocketClientDto desiredState) {
        currentState.updateSocket(
                desiredState.getSocketCode(),
                SocketTypeDto.fromValue(desiredState.getType().getValue())
        );
        return currentState;
    }

    @Override
    protected Socket createDefault(Long key) {
        return new Socket(key);
    }
}
