package it.polimi.cpms.bookingservice.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.cpms.bookingservice.configuration.JacksonObjectMapperConfig;

public class ChargingStationDtoMapper {
    static ObjectMapper objectMapper =  new JacksonObjectMapperConfig().jsonObjectMapper();

}
