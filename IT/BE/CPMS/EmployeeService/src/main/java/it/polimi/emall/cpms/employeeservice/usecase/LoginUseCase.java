package it.polimi.emall.cpms.employeeservice.usecase;


import it.polimi.emall.cpms.employeeservice.generated.http.server.model.EmployeeDto;
import it.polimi.emall.cpms.employeeservice.mapper.EmployeeMapper;
import it.polimi.emall.cpms.employeeservice.model.Employee;
import it.polimi.emall.cpms.employeeservice.model.EmployeeManager;
import it.polimi.emall.cpms.employeeservice.utils.JwtHelper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class LoginUseCase {
    public final EmployeeManager employeeManager;
    private final JwtHelper jwtHelper;


    public LoginUseCase(EmployeeManager employeeManager, JwtHelper jwtHelper) {
        this.employeeManager = employeeManager;
        this.jwtHelper = jwtHelper;
    }

    public EmployeeDto login(String email, String password){
        try {
            Employee employee = employeeManager.getByEmailAddress(email);
            if (!employee.getPassword().equals(password))
                throw new NoSuchElementException();
            return EmployeeMapper.buildEmployeeDto(employee, jwtHelper.generateToken(employee.getId()));


        }catch (NoSuchElementException e){
            throw new NoSuchElementException("Username and/or password are not correct");
        }
    }

    public EmployeeDto getEmployeeById(Long employeeId){
        return EmployeeMapper.buildEmployeeDto(employeeManager.getById(employeeId), jwtHelper.generateToken(employeeId));
    }


}
