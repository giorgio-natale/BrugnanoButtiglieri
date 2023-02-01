package it.polimi.emall.emsp.customerservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class CustomerTokenDto {
    public final String key;
    public final Long customerId;

    @JsonCreator
    public CustomerTokenDto(String key, Long customerId) {
        this.key = key;
        this.customerId = customerId;
    }
}
