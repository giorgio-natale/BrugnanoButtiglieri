package it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.emall.cpms.chargingmanagementservice.configuration.JacksonObjectMapperConfig;
import jakarta.persistence.AttributeConverter;

public class ProgressInformationConverter implements AttributeConverter<ProgressInformation, String> {
    private static final ObjectMapper objectMapper = new JacksonObjectMapperConfig().jsonObjectMapper();
    @Override
    public String convertToDatabaseColumn(ProgressInformation progressInformation) {
        if(progressInformation == null)
            return null;
        try {
            return objectMapper.writeValueAsString(progressInformation);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProgressInformation convertToEntityAttribute(String s) {
        if(s == null)
            return null;
        try {
            return objectMapper.readValue(s, ProgressInformation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
