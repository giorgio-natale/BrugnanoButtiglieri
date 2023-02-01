package it.polimi.emall.emsp.bookingmanagementservice.model.customerdevice;

import it.polimi.emall.emsp.bookingmanagementservice.utils.Identifiable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Device implements Identifiable<Long> {
    @Id
    private Long customerId;

    private String expoToken;

    public Device(Long customerId){
        this.customerId = customerId;
    }

    public void setExpoToken(String expoToken){
        this.expoToken = expoToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Device device = (Device) o;
        return Objects.equals(getId(), device.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Device.class);
    }

    @Override
    public Long getId() {
        return customerId;
    }
}
