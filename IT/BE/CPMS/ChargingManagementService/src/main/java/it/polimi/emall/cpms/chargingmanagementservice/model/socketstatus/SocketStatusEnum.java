package it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus;

public enum SocketStatusEnum {
    SocketAvailableStatus,
    SocketReadyStatus,
    SocketDeliveringStatus,
    SocketStoppedStatus;

    private SocketStatusEnum parent;

    static {
        SocketAvailableStatus.parent = SocketStoppedStatus;
        SocketReadyStatus.parent = SocketAvailableStatus;
        SocketDeliveringStatus.parent = SocketReadyStatus;
        SocketStoppedStatus.parent = SocketDeliveringStatus;
    }

    public SocketStatusEnum getParent(){
        return parent;
    }
}
