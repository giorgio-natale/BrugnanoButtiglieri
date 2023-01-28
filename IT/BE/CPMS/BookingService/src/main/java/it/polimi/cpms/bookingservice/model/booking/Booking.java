package it.polimi.cpms.bookingservice.model.booking;

import it.polimi.cpms.bookingservice.utils.Identifiable;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingTypeDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.SocketTypeDto;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_booking")
    private Long id;
    private String bookingCode;
    @Enumerated(EnumType.STRING)
    private BookingTypeDto bookingType;
    private Long chargingStationId;
    private Long customerId;
    private Long chargingPointId;
    private Long socketId;
    private SocketTypeDto socketType;
    @Embedded
    private TimeFrame timeFrame;

    @OneToOne
    @PrimaryKeyJoinColumn
    private BookingStatus bookingStatus;

    void updateBooking(String bookingCode, BookingTypeDto bookingType, Long chargingStationId,
                       Long customerId, Long chargingPointId, Long socketId, SocketTypeDto socketType, TimeFrame timeFrame){
        this.bookingCode = bookingCode;
        this.bookingType = bookingType;
        this.chargingStationId = chargingStationId;
        this.customerId = customerId;
        this.chargingPointId = chargingPointId;
        this.socketId = socketId;
        this.socketType = socketType;
        this.timeFrame = timeFrame;
    }

    void setBookingStatus(BookingStatus bookingStatus){
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
