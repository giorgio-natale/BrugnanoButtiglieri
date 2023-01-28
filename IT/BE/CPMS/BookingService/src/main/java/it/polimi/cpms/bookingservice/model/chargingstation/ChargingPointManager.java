package it.polimi.cpms.bookingservice.model.chargingstation;

import it.polimi.cpms.bookingservice.utils.IdAssignedManager;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.model.ChargingPointClientDto;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.model.SocketClientDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class ChargingPointManager extends IdAssignedManager<ChargingPoint, Long, ChargingPointClientDto> {
    private final SocketManager socketManager;
    private final ChargingPointRepository chargingPointRepository;
    protected ChargingPointManager(ChargingPointRepository chargingPointRepository, SocketManager socketManager) {
        super(chargingPointRepository);
        this.socketManager = socketManager;
        this.chargingPointRepository = chargingPointRepository;
    }

    public Optional<ChargingPoint> findChargingPointOwningSocket(Socket socket){
        return chargingPointRepository.findChargingPointBySocketsContaining(socket);
    }

    @Override
    protected ChargingPoint updateEntity(ChargingPoint currentState, ChargingPointClientDto desiredState) {
        Set<Socket> oldSockets = currentState.getSockets();
        currentState.clearSockets();

        Set<Long> newSocketIds = desiredState.getSocketList()
                .stream()
                .map(SocketClientDto::getSocketId)
                .collect(Collectors.toSet());

        oldSockets
                .stream()
                .filter(socket -> !newSocketIds.contains(socket.getId()))
                .forEach(socket -> socketManager.delete(socket.getId()));

        Set<Socket> newSockets = new HashSet<>();
        desiredState.getSocketList().forEach(socketClientDto ->
            newSockets.add(socketManager.getOrCreateNewAndUpdate(socketClientDto.getSocketId(), socketClientDto))
        );

        currentState.updateChargingPoint(
                desiredState.getChargingPointCode(),
                desiredState.getMode(),
                newSockets
        );

        return currentState;
    }

    @Override
    protected ChargingPoint createDefault(Long key) {
        return new ChargingPoint(key);
    }

    @Override
    protected void preDelete(Long key) {
        chargingPointRepository.findById(key).ifPresent(chargingPoint -> {
            Set<Socket> socketsToDelete = chargingPoint.getSockets();
            chargingPoint.clearSockets();
            socketsToDelete.forEach(socket -> socketManager.delete(socket.getId()));
        });
    }
}
