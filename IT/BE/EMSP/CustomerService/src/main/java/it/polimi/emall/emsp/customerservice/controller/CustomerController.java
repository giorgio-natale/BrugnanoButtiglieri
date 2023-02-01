package it.polimi.emall.emsp.customerservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.polimi.emall.emsp.customerservice.generated.http.server.controller.CustomerApi;
import it.polimi.emall.emsp.customerservice.generated.http.server.model.CustomerDto;
import it.polimi.emall.emsp.customerservice.generated.http.server.model.LoginRequestDto;
import it.polimi.emall.emsp.customerservice.generated.http.server.model.SignupRequestDto;
import it.polimi.emall.emsp.customerservice.model.CustomerTokenDto;
import it.polimi.emall.emsp.customerservice.usecase.LoginUseCase;
import it.polimi.emall.emsp.customerservice.usecase.SignupUseCase;
import it.polimi.emall.emsp.customerservice.utils.JwtHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController implements CustomerApi {
    private final SignupUseCase signupUseCase;
    private final LoginUseCase loginUseCase;

    private final JwtHelper jwtHelper;

    public CustomerController(SignupUseCase signupUseCase, LoginUseCase loginUseCase, JwtHelper jwtHelper) {
        this.signupUseCase = signupUseCase;
        this.loginUseCase = loginUseCase;
        this.jwtHelper = jwtHelper;
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @Override
    public ResponseEntity<CustomerDto> getCustomerCustomerId(Long customerId) {
        CustomerTokenDto customerTokenDto = jwtHelper.buildTokenDtoFromRequest();
        if(!customerTokenDto.customerId.equals(customerId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(
                loginUseCase.getCustomerById(customerId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<CustomerDto> login(LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(loginUseCase.login(loginRequestDto.getEmailAddress(), loginRequestDto.getPassword()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> signup(SignupRequestDto signupRequestDto) {
        signupUseCase.signup(signupRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
