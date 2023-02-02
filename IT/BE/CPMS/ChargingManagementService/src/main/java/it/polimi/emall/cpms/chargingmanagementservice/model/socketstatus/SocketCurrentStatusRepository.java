package it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Set;

public interface SocketCurrentStatusRepository extends CrudRepository<SocketCurrentStatus, Long>, Repository<SocketCurrentStatus, Long> {
    Set<SocketCurrentStatus> findAllByChargingStationId(Long chargingStationId);
}
