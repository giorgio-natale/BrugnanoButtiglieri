package it.polimi.emall.cpms.chargingmanagementservice.model.booking;

import it.polimi.emall.cpms.chargingmanagementservice.utils.Identifiable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Booking implements Identifiable<Long> {
    @Id
    private Long id;
    private String bookingCode;
    private Long chargingStationId;
    private Long customerId;
    private Long chargingPointId;
    private Long socketId;

    @Enumerated(EnumType.STRING)
    private BookingStatusEnum bookingStatus;

    public Booking(Long bookingId){
        this.id = bookingId;
    }

    void updateBooking(String bookingCode, Long chargingStationId,
                       Long customerId, Long chargingPointId, Long socketId,
                       BookingStatusEnum bookingStatus
    ){
        this.bookingCode = bookingCode;
        this.chargingStationId = chargingStationId;
        this.customerId = customerId;
        this.chargingPointId = chargingPointId;
        this.socketId = socketId;
        this.bookingStatus = bookingStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Booking booking = (Booking) o;
        return Objects.equals(getId(), booking.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Booking.class);
    }
}
