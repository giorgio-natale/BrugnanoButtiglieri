package it.polimi.emall.emsp.bookingmanagementservice.model.customerdevice;

import it.polimi.emall.emsp.bookingmanagementservice.utils.IdAssignedManager;
import org.springframework.stereotype.Component;

@Component
public class DeviceManager extends IdAssignedManager<Device, Long, String> {
    protected DeviceManager(DeviceRepository crudRepository) {
        super(crudRepository);
    }

    @Override
    protected Device updateEntity(Device currentState, String desiredState) {
        currentState.setExpoToken(desiredState);
        return currentState;
    }

    @Override
    protected Device createDefault(Long key) {
        return new Device(key);
    }
}
