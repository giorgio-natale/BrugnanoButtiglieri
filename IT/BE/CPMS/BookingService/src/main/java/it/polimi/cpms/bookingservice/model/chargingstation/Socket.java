package it.polimi.cpms.bookingservice.model.chargingstation;

import it.polimi.cpms.bookingservice.utils.Identifiable;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.SocketTypeDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Socket implements Identifiable<Long> {
    @Id
    private Long id;

    private String socketCode;
    private SocketTypeDto type;

    public Socket(Long id){
        this.id = id;
    }
    void updateSocket(String socketCode, SocketTypeDto type){
        this.socketCode = socketCode;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Socket socket = (Socket) o;
        return Objects.equals(this.getId(), socket.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Socket.class);
    }

}
