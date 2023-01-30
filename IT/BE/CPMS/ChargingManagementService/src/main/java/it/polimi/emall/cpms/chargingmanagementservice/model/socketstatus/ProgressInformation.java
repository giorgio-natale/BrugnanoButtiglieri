package it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus;

import java.io.Serializable;

public record ProgressInformation(Integer expectedMinutesLeft, Double kWhAbsorbed) implements Serializable {
}
