package it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus;

import it.polimi.emall.cpms.chargingmanagementservice.utils.Identifiable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocketCurrentStatus implements Identifiable<Long> {
    @Id
    private Long id;
    private Long chargingPointId;
    private Long chargingStationId;
    @Enumerated(EnumType.STRING)
    private SocketStatusEnum socketStatusEnum;
    @Convert(converter = ProgressInformationConverter.class)
    private ProgressInformation progressInformation;

    public SocketCurrentStatus(Long id){
        this.id = id;
        this.socketStatusEnum = SocketStatusEnum.SocketAvailableStatus;
    }

    void updateSocketInfo(Long chargingPointId, Long chargingStationId){
        this.chargingPointId = chargingPointId;
        this.chargingStationId = chargingStationId;
    }

    void updateStatus(SocketStatusEnum socketStatusEnum, ProgressInformation progressInformation){
        if(socketStatusEnum != this.socketStatusEnum && socketStatusEnum.getParent() != this.socketStatusEnum)
            throw new IllegalStateException(String.format(
                    "Cannot transition from status %s to status %s",
                    this.socketStatusEnum,
                    socketStatusEnum
            ));
        if(socketStatusEnum.equals(SocketStatusEnum.SocketDeliveringStatus) && progressInformation == null)
            throw new IllegalArgumentException("Progress information are needed when status is SocketDeliveringStatus");
        if(!socketStatusEnum.equals(SocketStatusEnum.SocketDeliveringStatus) && progressInformation != null)
            throw new IllegalArgumentException("Progress information cannot be set when status is not SocketDeliveringStatus");

        this.socketStatusEnum = socketStatusEnum;
        this.progressInformation = progressInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SocketCurrentStatus socketCurrentStatus = (SocketCurrentStatus) o;
        return Objects.equals(getId(), socketCurrentStatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(SocketCurrentStatus.class);
    }

}
