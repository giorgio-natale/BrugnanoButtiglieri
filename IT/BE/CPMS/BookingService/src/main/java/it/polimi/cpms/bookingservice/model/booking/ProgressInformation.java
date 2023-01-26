package it.polimi.cpms.bookingservice.model.booking;

import java.io.Serializable;

public record ProgressInformation(Integer expectedMinutesLeft) implements Serializable {
}
