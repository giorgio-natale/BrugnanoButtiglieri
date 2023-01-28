package it.polimi.cpms.bookingservice.model.chargingstation;

import it.polimi.cpms.bookingservice.utils.IdAssignedManager;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.model.ChargingPointClientDto;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.model.ChargingStationClientDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChargingStationManager extends IdAssignedManager<ChargingStation, Long, ChargingStationClientDto> {
    private final ChargingPointManager chargingPointManager;
    private final ChargingStationRepository chargingStationRepository;

    protected ChargingStationManager(ChargingStationRepository chargingStationRepository, ChargingPointManager chargingPointManager) {
        super(chargingStationRepository);
        this.chargingPointManager = chargingPointManager;
        this.chargingStationRepository = chargingStationRepository;
    }

    public ChargingPoint getChargingPoint(ChargingStation chargingStation, Long chargingPointId){
        return chargingStation.getChargingPoints().stream().filter(chargingPoint -> chargingPoint.getId().equals(chargingPointId)).findFirst().orElseThrow();
    }

    public ChargingPoint findChargingPointOwningSocket(Socket socket){
        return chargingPointManager.findChargingPointOwningSocket(socket).orElseThrow();
    }

    @Override
    protected ChargingStation updateEntity(ChargingStation currentState, ChargingStationClientDto desiredState) {
        Set<ChargingPoint> oldChargingPoints = currentState.getChargingPoints();
        currentState.clearChargingPoints();

        Set<Long> newChargingPointIds = desiredState.getChargingPointList()
                .stream()
                .map(ChargingPointClientDto::getChargingPointId)
                .collect(Collectors.toSet());

        oldChargingPoints
                .stream()
                .filter(socket -> !newChargingPointIds.contains(socket.getId()))
                .forEach(socket -> chargingPointManager.delete(socket.getId()));

        Set<ChargingPoint> newChargingPoints = new HashSet<>();
        desiredState.getChargingPointList().forEach(chargingPointClientDto ->
                newChargingPoints.add(
                        chargingPointManager.getOrCreateNewAndUpdate(chargingPointClientDto.getChargingPointId(), chargingPointClientDto)
                )
        );

        currentState.updateChargingStation(
                desiredState.getName(),
                desiredState.getCity(),
                desiredState.getAddress(),
                newChargingPoints
        );

        return currentState;

    }

    @Override
    protected ChargingStation createDefault(Long key) {
        return new ChargingStation(key);
    }

    @Override
    protected void preDelete(Long key) {
        chargingStationRepository.findById(key).ifPresent(chargingStation -> {
            Set<ChargingPoint> chargingPointsToDelete = chargingStation.getChargingPoints();
            chargingStation.clearChargingPoints();
            chargingPointsToDelete.forEach(socket -> chargingPointManager.delete(socket.getId()));
        });
    }
}
