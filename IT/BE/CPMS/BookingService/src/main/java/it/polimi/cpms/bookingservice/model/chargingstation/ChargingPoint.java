package it.polimi.cpms.bookingservice.model.chargingstation;

import it.polimi.cpms.bookingservice.model.booking.Booking;
import it.polimi.cpms.bookingservice.utils.Identifiable;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.model.ChargingPointModeClientDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
public class ChargingPoint implements Identifiable<Long> {
    @Id
    private Long id;
    private String chargingPointCode;
    @Enumerated(EnumType.STRING)
    private ChargingPointModeClientDto mode;

    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Socket> sockets;

    public ChargingPoint(){
        this(null);
    }
    public ChargingPoint(Long id){
        this.id = id;
        this.sockets = new HashSet<>();
    }

    void updateChargingPoint(String chargingPointCode,
                             ChargingPointModeClientDto mode,
                             Set<Socket> sockets){
        clearSockets();
        this.sockets.addAll(sockets);
        this.chargingPointCode = chargingPointCode;
        this.mode = mode;

    }

    public void clearSockets(){
        this.sockets.clear();
    }

    public Set<Socket> getSockets(){
        return new HashSet<>(this.sockets);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChargingPoint chargingPoint = (ChargingPoint) o;
        return Objects.equals(this.getId(), chargingPoint.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ChargingPoint.class);
    }
}
