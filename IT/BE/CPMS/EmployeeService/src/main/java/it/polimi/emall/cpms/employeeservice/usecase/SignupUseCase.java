package it.polimi.emall.cpms.employeeservice.usecase;


import it.polimi.emall.cpms.employeeservice.generated.http.server.model.SignupRequestDto;
import it.polimi.emall.cpms.employeeservice.model.EmployeeManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupUseCase {
    private final EmployeeManager employeeManager;

    public SignupUseCase(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    @Transactional
    public void signup(Long id, SignupRequestDto signupRequestDto){
        employeeManager.getOrCreateNewAndUpdate(id, signupRequestDto);
    }

}
