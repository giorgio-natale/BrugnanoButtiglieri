package it.polimi.cpms.bookingservice.model.cpocatalog;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CpoRepository extends CrudRepository<Cpo, Long>, Repository<Cpo, Long> {
    Optional<Cpo> findCpoByOwnedChargingStationIdsContains(Long chargingStationId);
}
