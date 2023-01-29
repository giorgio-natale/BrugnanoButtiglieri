package it.polimi.emall.cpms.chargingmanagementservice.model.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface BookingRepository extends CrudRepository<Booking, Long>, Repository<Booking, Long> {

}
