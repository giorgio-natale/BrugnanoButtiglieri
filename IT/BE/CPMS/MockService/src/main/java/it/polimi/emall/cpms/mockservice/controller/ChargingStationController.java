package it.polimi.emall.cpms.mockservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.emall.cpms.mockservice.configuration.JacksonObjectMapperConfig;
import it.polimi.emall.cpms.mockservice.generated.http.server.controller.ChargingStationApi;
import it.polimi.emall.cpms.mockservice.generated.http.server.model.ChargingStationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChargingStationController implements ChargingStationApi {
    private final Resource chargingStationConfiguration;
    private final ObjectMapper mapper = new JacksonObjectMapperConfig().jsonObjectMapper();

    public ChargingStationController(@Value("classpath:chargingStations.json") Resource chargingStationConfiguration) {
        this.chargingStationConfiguration = chargingStationConfiguration;
    }

    @Override
    public ResponseEntity<ChargingStationDto> getChargingStationConfiguration(Long chargingStationId) {
        try {
            List<ChargingStationDto> chargingStationList =
                    mapper.readValue(chargingStationConfiguration.getInputStream(), new TypeReference<>() {});
            return new ResponseEntity<>(chargingStationList.get(0), HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<ChargingStationDto>> getChargingStationConfigurationList() {
        try {
            List<ChargingStationDto> chargingStationList =
                    mapper.readValue(chargingStationConfiguration.getInputStream(), new TypeReference<>() {});
            return new ResponseEntity<>(chargingStationList, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
