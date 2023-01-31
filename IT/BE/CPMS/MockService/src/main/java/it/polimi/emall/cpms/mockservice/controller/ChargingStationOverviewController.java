package it.polimi.emall.cpms.mockservice.controller;

import it.polimi.emall.cpms.mockservice.generated.http.server.controller.ChargingStationApi;
import it.polimi.emall.cpms.mockservice.generated.http.server.controller.ChargingStationOverviewApi;
import it.polimi.emall.cpms.mockservice.generated.http.server.model.ChargingStationDto;
import it.polimi.emall.cpms.mockservice.generated.http.server.model.ChargingStationOverviewDto;
import it.polimi.emall.cpms.mockservice.generated.http.server.model.PricingDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class ChargingStationOverviewController implements ChargingStationOverviewApi {
    private final ChargingStationApi chargingStationApi;

    public ChargingStationOverviewController(ChargingStationApi chargingStationApi) {
        this.chargingStationApi = chargingStationApi;
    }

    @Override
    public ResponseEntity<ChargingStationOverviewDto> getChargingStationOverview(Long chargingStationId) {
        ChargingStationDto chargingStationDto = chargingStationApi.getChargingStationConfiguration(chargingStationId).getBody();
        PricingDto pricingDto = chargingStationApi.getPricing(chargingStationId).getBody();
        assert chargingStationDto != null;
        assert pricingDto != null;
        return new ResponseEntity<>(
                new ChargingStationOverviewDto(
                        chargingStationDto.getChargingStationId(),
                        chargingStationDto.getName(),
                        chargingStationDto.getAddress(),
                        chargingStationDto.getCity(),
                        chargingStationDto.getLatitude(),
                        chargingStationDto.getLongitude(),
                        pricingDto.getPrice(),
                        pricingDto.getPercentageOffer()
                ), HttpStatus.OK
        );

    }

    @Override
    public ResponseEntity<List<ChargingStationOverviewDto>> getChargingStationOverviewList() {
        Map<Long, ChargingStationDto> chargingStationDtos =
                Objects.requireNonNull(chargingStationApi.getChargingStationConfigurationList().getBody())
                        .stream()
                        .collect(Collectors.toMap(
                                ChargingStationDto::getChargingStationId,
                                station->station
                        ));
        Map<Long, PricingDto> chargingStationPricingDto =
                Objects.requireNonNull(chargingStationApi.getPricingList().getBody())
                        .stream()
                        .collect(Collectors.toMap(
                                PricingDto::getChargingStationId,
                                pricing->pricing
                        ));
        return new ResponseEntity<>(
                chargingStationDtos.entrySet()
                        .stream()
                        .map(entry -> {
                            PricingDto pricingDto = chargingStationPricingDto.get(entry.getKey());
                            return new ChargingStationOverviewDto(
                                    entry.getKey(),
                                    entry.getValue().getName(),
                                    entry.getValue().getAddress(),
                                    entry.getValue().getCity(),
                                    entry.getValue().getLatitude(),
                                    entry.getValue().getLongitude(),
                                    pricingDto.getPrice(),
                                    pricingDto.getPercentageOffer()
                            );
                        })
                        .collect(Collectors.toList()),
                HttpStatus.OK
                );
    }
}
