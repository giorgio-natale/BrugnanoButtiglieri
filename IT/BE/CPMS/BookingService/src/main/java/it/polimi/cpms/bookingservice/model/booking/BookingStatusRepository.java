package it.polimi.cpms.bookingservice.model.booking;

import org.springframework.data.repository.CrudRepository;

public interface BookingStatusRepository extends CrudRepository<BookingStatus, Long> {
}
