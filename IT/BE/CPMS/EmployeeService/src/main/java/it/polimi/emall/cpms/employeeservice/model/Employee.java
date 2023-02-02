package it.polimi.emall.cpms.employeeservice.model;

import it.polimi.emall.cpms.employeeservice.utils.Identifiable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Employee implements Identifiable<Long> {
    @Id
    private Long id;

    @Column(unique = true)
    private String emailAddress;
    private String password;
    private String name;
    private String surname;

    public Employee(Long id){
        this.id = id;
    }

    void updateEmployeeInfo(
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
        Employee employee = (Employee) o;
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Employee.class);
    }
}
