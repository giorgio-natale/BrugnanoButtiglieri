package it.polimi.emall.emsp.bookingmanagementservice.model.booking;

import java.io.Serializable;

public record ProgressInformation(Integer expectedMinutesLeft) implements Serializable {
}
