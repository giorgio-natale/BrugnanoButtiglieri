package it.polimi.emall.emsp.customerservice.model;

import it.polimi.emall.emsp.customerservice.generated.http.server.model.SignupRequestDto;
import it.polimi.emall.emsp.customerservice.utils.IdGeneratedManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerManager extends IdGeneratedManager<Customer, Long, SignupRequestDto> {
    private final CustomerRepository customerRepository;
    protected CustomerManager(CustomerRepository customerRepository) {
        super(customerRepository);
        this.customerRepository = customerRepository;
    }

    public Customer getByEmailAddress(String emailAddress){
        return customerRepository.findByEmailAddress(emailAddress).orElseThrow();
    }

    @Override
    protected Customer updateEntity(Customer currentState, SignupRequestDto desiredState) {
        customerRepository.findByEmailAddress(desiredState.getEmailAddress())
                        .ifPresent(customer -> {
                            if(!customer.equals(currentState))
                                throw new IllegalStateException(String.format(
                                        "Another customer already exists with email %s",
                                        desiredState.getEmailAddress()));
                        });


        currentState.updateCustomerInfo(
                desiredState.getEmailAddress(),
                desiredState.getPassword(),
                desiredState.getName(),
                desiredState.getSurname()
        );
        return currentState;
    }

    @Override
    protected Customer createDefault() {
        return new Customer();
    }
}
