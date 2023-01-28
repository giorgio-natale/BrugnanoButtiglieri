package it.polimi.cpms.bookingservice.model.chargingstation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ChargingPointRepository extends CrudRepository<ChargingPoint, Long>, Repository<ChargingPoint, Long> {
    Optional<ChargingPoint> findChargingPointBySocketsContaining(Socket socket);
}
