package it.polimi.emall.cpms.employeeservice.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Optional<Employee> findByEmailAddress(String emailAddress);
}
