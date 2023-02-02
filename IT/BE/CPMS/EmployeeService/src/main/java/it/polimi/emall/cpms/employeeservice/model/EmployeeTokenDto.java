package it.polimi.emall.cpms.employeeservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class EmployeeTokenDto {
    public final String key;
    public final Long employeeId;

    @JsonCreator
    public EmployeeTokenDto(String key, Long employeeId) {
        this.key = key;
        this.employeeId = employeeId;
    }
}
