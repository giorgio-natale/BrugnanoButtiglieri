package it.polimi.emall.emsp.bookingmanagementservice.model.cpocatalog;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class CpoDto {
    private Long id;
    private String basePath;
    private Set<Long> chargingStationIds;
}
