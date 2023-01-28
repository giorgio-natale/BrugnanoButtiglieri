package it.polimi.cpms.bookingservice.model.chargingstation;

import it.polimi.cpms.bookingservice.utils.Identifiable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
public class ChargingStation implements Identifiable<Long> {
    @Id
    private Long id;

    private String name;
    private String city;
    private String address;

    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChargingPoint> chargingPoints;

    protected ChargingStation(){
        this(null);
    }

    public ChargingStation(Long id){
        this.id = id;
        this.chargingPoints = new HashSet<>();
    }

    void updateChargingStation(
            String name, String city,
            String address, Set<ChargingPoint> chargingPoints
    ){
        clearChargingPoints();
        this.chargingPoints.addAll(chargingPoints);
        this.name = name;
        this.city = city;
        this.address = address;
    }

    void clearChargingPoints(){
        this.chargingPoints.clear();
    }

    public Set<ChargingPoint> getChargingPoints(){
        return new HashSet<>(this.chargingPoints);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChargingStation chargingStation = (ChargingStation) o;
        return Objects.equals(this.getId(), chargingStation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ChargingStation.class);
    }

}
