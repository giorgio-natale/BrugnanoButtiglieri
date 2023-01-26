package it.polimi.cpms.bookingservice.model.cpocatalog;

import it.polimi.cpms.bookingservice.utils.IdAssignedManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class CpoManager extends IdAssignedManager<Cpo, Long, CpoDto> {
    private final CpoRepository cpoRepository;

    protected CpoManager(CpoRepository cpoRepository) {
        super(cpoRepository);
        this.cpoRepository = cpoRepository;
    }

    @Override
    protected Cpo updateEntity(Cpo currentState, CpoDto desiredState) {
        currentState.setCpmsBasePath(desiredState.getBasePath());
        currentState.setOwnedChargingStationIds(desiredState.getChargingStationIds());
        return currentState;
    }

    @Override
    protected Cpo createDefault(Long key) {
        return new Cpo(key, new HashSet<>(), null);
    }

    public String getCpmsBasePathFromChargingStationId(Long chargingStationId){
        return cpoRepository
                .findCpoByOwnedChargingStationIdsContains(chargingStationId)
                .map(Cpo::getCpmsBasePath)
                .orElseThrow();
    }
}
