package it.polimi.cpms.bookingservice.model.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Set;

public interface BookingRepository extends CrudRepository<Booking, Long>, Repository<Booking, Long> {
    @Query("""
               SELECT booking
               FROM Booking booking JOIN BookingStatus status
               WHERE booking.id = status.id AND (status.bookingStatus = 'BookingStatusPlanned' OR status.bookingStatus = 'BookingStatusInProgress')
                     AND booking.bookingType = 'IN_ADVANCE'
                     AND :#{#timeFrame.startInstant} < booking.timeFrame.endInstant
                     AND :#{#timeFrame.endInstant} > booking.timeFrame.startInstant
                     AND booking.chargingStationId = :chargingStationId
          """)
    Set<Booking> findIntersectingBookingsInAdvance(@Param("chargingStationId") Long chargingStationId, @Param("timeFrame") TimeFrame timeFrame);

    @Query("""
                SELECT CASE WHEN (COUNT(booking) > 0)  THEN TRUE ELSE FALSE END
                FROM Booking booking
                WHERE   booking.chargingStationId = :chargingStationId
                    AND booking.chargingPointId = :chargingPointId
                    AND booking.socketId = :socketId
                    AND (booking.bookingStatus.bookingStatus = 'BookingStatusPlanned' OR booking.bookingStatus.bookingStatus = 'BookingStatusInProgress')
                    AND booking.bookingType = 'ON_THE_FLY'

           """)
    boolean isBookingOnTheFlyOccupied(
            @Param("chargingStationId") Long chargingStationId,
            @Param("chargingPointId") Long chargingPointId,
            @Param("socketId") Long socketId
    );

    @Query("""
                SELECT booking
                FROM Booking booking
                WHERE   booking.bookingType = 'IN_ADVANCE'
                    AND (booking.bookingStatus.bookingStatus = 'BookingStatusPlanned' OR booking.bookingStatus.bookingStatus = 'BookingStatusInProgress')
                    AND booking.timeFrame.endInstant < :offsetDateTime
           """)
    Set<Booking> findBookingsExceedingDate(@Param("offsetDateTime") OffsetDateTime offsetDateTime);
}
