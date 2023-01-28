package it.polimi.cpms.bookingservice.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.cpms.bookingservice.configuration.JacksonObjectMapperConfig;
import it.polimi.cpms.bookingservice.model.booking.TimeFrame;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.TimeframeDto;

public class CommonMapper {
    private final static ObjectMapper objectMapper =  new JacksonObjectMapperConfig().jsonObjectMapper();

    public static TimeFrame buildTimeFrame(TimeframeDto timeframeDto){
        return new TimeFrame(timeframeDto.getStartInstant(), timeframeDto.getEndInstant());
    }

    public static TimeframeDto buildTimeFrameDto(TimeFrame timeFrame){
        return new TimeframeDto(timeFrame.getStartInstant()).endInstant(timeFrame.getEndInstant());
    }
}
