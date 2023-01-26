package it.polimi.cpms.bookingservice.model.cpocatalog;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class CpoDto {
    private Long id;
    private String basePath;
    private Set<Long> chargingStationIds;
}
