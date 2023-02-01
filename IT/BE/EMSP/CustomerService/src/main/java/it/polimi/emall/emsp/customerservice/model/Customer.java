package it.polimi.emall.emsp.customerservice.model;

import it.polimi.emall.emsp.customerservice.utils.Identifiable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Customer implements Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_customer")
    private Long id;

    @Column(unique = true)
    private String emailAddress;
    private String password;
    private String name;
    private String surname;

    void updateCustomerInfo(
            String emailAddress,
            String password,
            String name,
            String surname
    ){
        this.emailAddress = emailAddress;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Customer.class);
    }
}
