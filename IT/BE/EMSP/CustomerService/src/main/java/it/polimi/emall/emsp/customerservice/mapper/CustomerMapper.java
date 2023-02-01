package it.polimi.emall.emsp.customerservice.mapper;

import it.polimi.emall.emsp.customerservice.generated.http.server.model.CustomerDto;
import it.polimi.emall.emsp.customerservice.model.Customer;

public class CustomerMapper {
    public static CustomerDto buildCustomerDto(Customer customer, String token){
        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getSurname(),
                customer.getEmailAddress(),
                token
        );
    }
}
