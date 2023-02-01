package it.polimi.emall.cpms.mockservice.socketmock.model;

import it.polimi.emall.cpms.mockservice.generated.http.client.cpms_chargingmanagementservice.model.SocketStatusClientDto;
import it.polimi.emall.cpms.mockservice.generated.http.server.model.SocketStatusDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

public interface SocketMockRepository extends CrudRepository<SocketMock, Long>, Repository<SocketMock, Long> {

    Set<SocketMock> findSocketMocksBySocketStatusNot(SocketStatusDto socketStatusDto);
}
