package it.polimi.emall.emsp.bookingmanagementservice.usecases;

import com.fasterxml.jackson.annotation.JsonCreator;

public class NotificationDto {
    public final String to;
    public final String title;
    public final String body;
    public final NotificationDataDto data;

    @JsonCreator
    public NotificationDto(String expoToken, NotificationDataDto data) {
        this.to = expoToken;
        this.title = "Tutto è bene";
        this.body = "Quel che finisce bene \uD83D\uDD0B ";
        this.data = data;
    }
}
