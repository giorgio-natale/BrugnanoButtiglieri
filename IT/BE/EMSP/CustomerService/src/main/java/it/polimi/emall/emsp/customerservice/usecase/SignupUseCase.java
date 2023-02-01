package it.polimi.emall.emsp.customerservice.usecase;

import it.polimi.emall.emsp.customerservice.generated.http.server.model.SignupRequestDto;
import it.polimi.emall.emsp.customerservice.model.CustomerManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupUseCase {
    private final CustomerManager customerManager;

    public SignupUseCase(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    @Transactional
    public void signup(SignupRequestDto signupRequestDto){
        customerManager.createNewAndUpdate(signupRequestDto);
    }

}
