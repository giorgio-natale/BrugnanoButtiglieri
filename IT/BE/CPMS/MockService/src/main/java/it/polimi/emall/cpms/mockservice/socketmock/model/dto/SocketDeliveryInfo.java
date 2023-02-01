package it.polimi.emall.cpms.mockservice.socketmock.model.dto;

import it.polimi.emall.cpms.mockservice.generated.http.server.model.SocketStatusDto;

public class SocketDeliveryInfo {
    public final SocketStatusDto status;
    public final Integer initialKwHLeftToCharge;

    public SocketDeliveryInfo(SocketStatusDto status, Integer initialKwHLeftToCharge) {
        this.status = status;
        this.initialKwHLeftToCharge = initialKwHLeftToCharge;
    }

}
