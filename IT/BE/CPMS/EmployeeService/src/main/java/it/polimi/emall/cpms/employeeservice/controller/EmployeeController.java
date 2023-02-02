package it.polimi.emall.cpms.employeeservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.polimi.emall.cpms.employeeservice.generated.http.server.controller.EmployeeApi;
import it.polimi.emall.cpms.employeeservice.generated.http.server.model.EmployeeDto;
import it.polimi.emall.cpms.employeeservice.generated.http.server.model.LoginRequestDto;
import it.polimi.emall.cpms.employeeservice.usecase.LoginUseCase;
import it.polimi.emall.cpms.employeeservice.utils.JwtHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController implements EmployeeApi {
    private final LoginUseCase loginUseCase;
    private final JwtHelper jwtHelper;

    public EmployeeController(LoginUseCase loginUseCase, JwtHelper jwtHelper) {
        this.loginUseCase = loginUseCase;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public ResponseEntity<EmployeeDto> employeeLogin(LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(
                loginUseCase.login(loginRequestDto.getEmailAddress(), loginRequestDto.getPassword()),
                HttpStatus.OK
        );
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Override
    public ResponseEntity<EmployeeDto> getEmployee(Long employeeId) {
        if(!jwtHelper.buildTokenDtoFromRequest().employeeId.equals(employeeId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(
                loginUseCase.getEmployeeById(employeeId),
                HttpStatus.OK
        );
    }
}
