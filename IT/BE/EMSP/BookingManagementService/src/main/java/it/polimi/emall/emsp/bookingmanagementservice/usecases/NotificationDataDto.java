package it.polimi.emall.emsp.bookingmanagementservice.usecases;

import com.fasterxml.jackson.annotation.JsonCreator;

public class NotificationDataDto {
    public final String notificationType;
    public final Long bookingId;


    @JsonCreator
    public NotificationDataDto(Long bookingId) {
        this.bookingId = bookingId;
        this.notificationType = "BOOKING_COMPLETED";
    }
}
