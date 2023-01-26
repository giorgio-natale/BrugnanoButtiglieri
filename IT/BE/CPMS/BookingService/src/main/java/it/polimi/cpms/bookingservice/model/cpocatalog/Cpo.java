package it.polimi.cpms.bookingservice.model.cpocatalog;

import it.polimi.cpms.bookingservice.utils.Identifiable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cpo implements Identifiable<Long> {
    @Id
    private Long id;
    @ElementCollection
    private Set<Long> ownedChargingStationIds;
    private String cpmsBasePath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Cpo cpo = (Cpo) o;
        return Objects.equals(getId(), cpo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Cpo.class);
    }
}
