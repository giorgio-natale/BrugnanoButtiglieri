package it.polimi.emall.cpms.chargingmanagementservice.model.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

public interface BookingRepository extends CrudRepository<Booking, Long>, Repository<Booking, Long> {
    @Query(
            """
            SELECT booking
            FROM Booking booking
            WHERE booking.chargingStationId = :chargingStationId
            AND   booking.chargingPointId = :chargingPointId
            AND   booking.socketId = :socketId
            AND   (booking.bookingStatus = 'BookingStatusInProgress' OR booking.bookingStatus = 'BookingStatusPlanned')
            AND   (booking.timeFrame.endInstant IS NULL OR (booking.timeFrame.startInstant <= :targetDateTime AND booking.timeFrame.endInstant >= :targetDateTime))
            """
    )
    Optional<Booking> findCurrentBookingForDate(
            @Param("chargingStationId") Long chargingStationId,
            @Param("chargingPointId") Long chargingPointId,
            @Param("socketId") Long socketId,
            @Param("targetDateTime") OffsetDateTime targetDateTime);

    @Query(
            """
            SELECT booking
            FROM Booking booking
            WHERE booking.chargingStationId = :chargingStationId
            AND   booking.chargingPointId = :chargingPointId
            AND   booking.socketId = :socketId
            AND   booking.bookingStatus = :bookingStatus
            """
    )
    Optional<Booking> findCurrentBookingForState(
            @Param("chargingStationId") Long chargingStationId,
            @Param("chargingPointId") Long chargingPointId,
            @Param("socketId") Long socketId,
            @Param("bookingStatus") BookingStatusEnum bookingStatus);

    Set<Booking> findAllByChargingStationId(Long chargingStationId);


}
