package it.polimi.emall.cpms.mockservice.socketmock.model;

import it.polimi.emall.cpms.mockservice.generated.http.client.cpms_chargingmanagementservice.model.SocketStatusClientDto;
import it.polimi.emall.cpms.mockservice.generated.http.server.model.SocketStatusDto;
import it.polimi.emall.cpms.mockservice.generated.http.server.model.SocketTypeDto;
import it.polimi.emall.cpms.mockservice.socketmock.model.dto.SocketDeliveryInfo;
import it.polimi.emall.cpms.mockservice.utils.Identifiable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocketMock implements Identifiable<Long> {

    private static final Map<SocketTypeDto, Integer> kwBySocketType = Map.of(
            SocketTypeDto.SLOW, 10,
            SocketTypeDto.FAST, 20,
            SocketTypeDto.RAPID, 50
    );

    private static final int defaultInitialKwHLeftToCharge = 20;

    @Id
    private Long socketId;
    private Long chargingPointId;
    private Long chargingStationId;

    @Enumerated(EnumType.STRING)
    private SocketTypeDto socketType;

    @Enumerated(EnumType.STRING)
    private SocketStatusDto socketStatus;

    private OffsetDateTime lastStatusChangeTime;

    private Integer initialKwHLeftToCharge;

    public SocketMock(Long id){
        this.socketId = id;
        this.socketStatus = SocketStatusDto.SOCKETAVAILABLESTATUS;
        this.lastStatusChangeTime = OffsetDateTime.now();
    }

    void updateConfiguration(Long chargingPointId, Long chargingStationId, SocketTypeDto socketType){
        this.chargingPointId = chargingPointId;
        this.chargingStationId = chargingStationId;
        this.socketType = socketType;
    }

    void setStatus(SocketDeliveryInfo socketDeliveryInfo){
        if(!this.socketStatus.equals(socketDeliveryInfo.status))
            this.lastStatusChangeTime = OffsetDateTime.now();
        if(socketDeliveryInfo.status.equals(SocketStatusDto.SOCKETREADYSTATUS))
            this.initialKwHLeftToCharge = socketDeliveryInfo.initialKwHLeftToCharge;
        this.socketStatus = socketDeliveryInfo.status;
    }

    public SocketStatusClientDto getUpdatedSocketStatusDto(
            OffsetDateTime now,
            int waitInReadyStateSeconds
    ){
        SocketStatusClientDto socketStatusClientDto = new SocketStatusClientDto();
        if(this.socketStatus.equals(SocketStatusDto.SOCKETREADYSTATUS)){
            if(Duration.between(this.getLastStatusChangeTime(), now).toSeconds() > waitInReadyStateSeconds){
                this.setStatus(new SocketDeliveryInfo(SocketStatusDto.SOCKETREADYSTATUS, defaultInitialKwHLeftToCharge));
                socketStatusClientDto
                        .status(SocketStatusDto.SOCKETDELIVERINGSTATUS.getValue())
                        .expectedMinutesLeft(60)
                        .kWhAbsorbed(0.0);
            }else{
                socketStatusClientDto.status(SocketStatusDto.SOCKETREADYSTATUS.getValue());
            }
        }
        else if(this.socketStatus.equals(SocketStatusDto.SOCKETDELIVERINGSTATUS)) {
            int deltaSeconds = (int) Duration.between(lastStatusChangeTime, now).toSeconds();
            double kwhConsumedSoFar = deltaSeconds * kwBySocketType.get(this.socketType) / 3600f;
            double remainingMinutes = 60 * (initialKwHLeftToCharge - kwhConsumedSoFar) / (float) kwBySocketType.get(this.socketType);
            if(remainingMinutes > 0){
                socketStatusClientDto
                        .status(SocketStatusDto.SOCKETDELIVERINGSTATUS.getValue())
                        .expectedMinutesLeft((int) remainingMinutes)
                        .kWhAbsorbed(kwhConsumedSoFar);
            }else{
                socketStatusClientDto
                        .status(SocketStatusDto.SOCKETSTOPPEDSTATUS.getValue());
            }

        } else if (this.socketStatus.equals(SocketStatusDto.SOCKETSTOPPEDSTATUS)) {
            socketStatusClientDto
                    .status(SocketStatusDto.SOCKETAVAILABLESTATUS.getValue());
        } else {
            socketStatusClientDto
                    .status(SocketStatusDto.SOCKETAVAILABLESTATUS.getValue());
        }
        return socketStatusClientDto;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SocketMock socketMock = (SocketMock) o;
        return Objects.equals(this.getSocketId(), socketMock.getSocketId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(SocketMock.class);
    }

    @Override
    public Long getId() {
        return getSocketId();
    }
}
