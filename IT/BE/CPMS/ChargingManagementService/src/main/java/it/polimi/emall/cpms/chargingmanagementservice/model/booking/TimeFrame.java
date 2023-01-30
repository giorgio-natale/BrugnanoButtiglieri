package it.polimi.emall.cpms.chargingmanagementservice.model.booking;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.Duration;
import java.time.OffsetDateTime;

@Embeddable
@Getter
public class TimeFrame {
    private OffsetDateTime startInstant;
    private OffsetDateTime endInstant;

    protected TimeFrame() {}
    public static TimeFrame getOpenTimeFrame(OffsetDateTime startInstant){
        if(Duration.between(startInstant, OffsetDateTime.now()).toSeconds() >= 60){
            throw new IllegalArgumentException("Check again the selected time frame");
        }
        TimeFrame tf = new TimeFrame();
        tf.startInstant = startInstant;
        return tf;
    }
    public static TimeFrame getClosedTimeFrame(OffsetDateTime startInstant, OffsetDateTime endInstant){
        if(Duration.between(startInstant, OffsetDateTime.now()).toSeconds() >= 60 || Duration.between(startInstant, endInstant).toSeconds() <= 60){
            throw new IllegalArgumentException("Check again the selected time frame");
        }
        TimeFrame tf = new TimeFrame();
        tf.startInstant = startInstant;
        tf.endInstant = endInstant;

        return tf;
    }
    public TimeFrame(OffsetDateTime startInstant, OffsetDateTime endInstant) {
        this.startInstant = startInstant;
        this.endInstant = endInstant;
    }


}
