package it.polimi.emall.cpms.employeeservice.mapper;


import it.polimi.emall.cpms.employeeservice.generated.http.server.model.EmployeeDto;
import it.polimi.emall.cpms.employeeservice.model.Employee;

public class EmployeeMapper {
    public static EmployeeDto buildEmployeeDto(Employee employee, String token){
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getSurname(),
                employee.getEmailAddress(),
                token
        );
    }
}
