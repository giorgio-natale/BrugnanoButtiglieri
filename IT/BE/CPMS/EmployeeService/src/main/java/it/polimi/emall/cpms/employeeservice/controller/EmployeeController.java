package it.polimi.emall.cpms.employeeservice.controller;

import it.polimi.emall.cpms.employeeservice.generated.http.server.controller.EmployeeApi;
import it.polimi.emall.cpms.employeeservice.generated.http.server.model.EmployeeDto;
import it.polimi.emall.cpms.employeeservice.generated.http.server.model.LoginRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController implements EmployeeApi {
    @Override
    public ResponseEntity<EmployeeDto> employeeLogin(LoginRequestDto loginRequestDto) {
        return EmployeeApi.super.employeeLogin(loginRequestDto);
    }

    @Override
    public ResponseEntity<EmployeeDto> getEmployee(Long employeeId) {
        return EmployeeApi.super.getEmployee(employeeId);
    }
}
