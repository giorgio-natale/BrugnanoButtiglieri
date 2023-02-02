package it.polimi.emall.emsp.bookingmanagementservice.usecases;

import it.polimi.emall.emsp.bookingmanagementservice.model.customerdevice.DeviceManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RegisterDeviceUseCase {
    private final DeviceManager deviceManager;


    public RegisterDeviceUseCase(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @Transactional
    public void registerDeviceManager(Long customerId, String expoToken){
        deviceManager.getOrCreateNewAndUpdate(customerId, expoToken);
    }
}
