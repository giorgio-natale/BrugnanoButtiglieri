package it.polimi.emall.emsp.bookingmanagementservice.model.booking;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Set;

public interface BookingRepository extends CrudRepository<Booking, Long>, Repository<Booking, Long> {
    Set<Booking> findAllByCustomerId(Long customerId);
}
