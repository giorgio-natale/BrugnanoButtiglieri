package it.polimi.cpms.bookingservice.model.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface BookingRepository extends CrudRepository<Booking, Long>, Repository<Booking, Long> {
    @Query("""
               SELECT booking
               FROM Booking booking JOIN BookingStatus status
               WHERE booking.id = status.id AND status.bookingStatus = 'BookingStatusPlanned' OR status.bookingStatus = 'BookingStatusInProgress'
                     AND booking.bookingType = 'IN_ADVANCE'
                     AND :#{#timeFrame.startInstant} < booking.timeFrame.endInstant
                     AND :#{#timeFrame.endInstant} > booking.timeFrame.startInstant
                     AND booking.chargingStationId = :chargingStationId
          """)
    Set<Booking> findIntersectingBookingsInAdvance(@Param("chargingStationId") Long chargingStationId, @Param("timeFrame") TimeFrame timeFrame);
}
