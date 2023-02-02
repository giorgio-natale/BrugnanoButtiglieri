package it.polimi.emall.cpms.employeeservice.model;

import it.polimi.emall.cpms.employeeservice.generated.http.server.model.SignupRequestDto;
import it.polimi.emall.cpms.employeeservice.utils.IdAssignedManager;
import org.springframework.stereotype.Component;

@Component
public class EmployeeManager extends IdAssignedManager<Employee, Long, SignupRequestDto> {
    private final EmployeeRepository employeeRepository;
    protected EmployeeManager(EmployeeRepository employeeRepository) {
        super(employeeRepository);
        this.employeeRepository = employeeRepository;
    }

    public Employee getByEmailAddress(String emailAddress){
        return employeeRepository.findByEmailAddress(emailAddress).orElseThrow();
    }

    @Override
    protected Employee updateEntity(Employee currentState, SignupRequestDto desiredState) {
        employeeRepository.findByEmailAddress(desiredState.getEmailAddress())
                        .ifPresent(employee -> {
                            if(!employee.equals(currentState))
                                throw new IllegalStateException(String.format(
                                        "Another employee already exists with email %s",
                                        desiredState.getEmailAddress()));
                        });


        currentState.updateEmployeeInfo(
                desiredState.getEmailAddress(),
                desiredState.getPassword(),
                desiredState.getName(),
                desiredState.getSurname()
        );
        return currentState;
    }


    @Override
    protected Employee createDefault(Long key) {
        return new Employee(key);
    }
}
