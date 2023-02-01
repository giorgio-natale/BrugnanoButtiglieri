package it.polimi.emall.emsp.customerservice.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByEmailAddress(String emailAddress);
}
