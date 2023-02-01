package it.polimi.emall.emsp.customerservice.usecase;

import it.polimi.emall.emsp.customerservice.generated.http.server.model.CustomerDto;
import it.polimi.emall.emsp.customerservice.mapper.CustomerMapper;
import it.polimi.emall.emsp.customerservice.model.Customer;
import it.polimi.emall.emsp.customerservice.model.CustomerManager;
import it.polimi.emall.emsp.customerservice.utils.JwtHelper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class LoginUseCase {
    public final CustomerManager customerManager;
    private final JwtHelper jwtHelper;


    public LoginUseCase(CustomerManager customerManager, JwtHelper jwtHelper) {
        this.customerManager = customerManager;
        this.jwtHelper = jwtHelper;
    }

    public CustomerDto login(String email, String password){
        try {
            Customer customer = customerManager.getByEmailAddress(email);
            if (!customer.getPassword().equals(password))
                throw new NoSuchElementException();
            return CustomerMapper.buildCustomerDto(customer, jwtHelper.generateToken(customer.getId()));


        }catch (NoSuchElementException e){
            throw new NoSuchElementException("Username and/or password are not correct");
        }
    }

    public CustomerDto getCustomerById(Long customerId){
        return CustomerMapper.buildCustomerDto(customerManager.getById(customerId), jwtHelper.generateToken(customerId));
    }


}
