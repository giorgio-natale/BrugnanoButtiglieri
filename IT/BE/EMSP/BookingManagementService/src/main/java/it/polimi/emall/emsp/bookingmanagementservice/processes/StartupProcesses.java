package it.polimi.emall.emsp.bookingmanagementservice.processes;

import it.polimi.emall.emsp.bookingmanagementservice.model.cpocatalog.CpoDto;
import it.polimi.emall.emsp.bookingmanagementservice.model.cpocatalog.CpoManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class StartupProcesses {
    private final boolean isLocalDeployment;
    private final String basePath;
    private final CpoManager cpoManager;

    public StartupProcesses(
            @Value("${debug.local-deployment:false}")boolean isLocalDeployment,
            @Value("${endpoints.booking-service.base-path:null}")String basePath,
            CpoManager cpoManager
    ) {
        this.isLocalDeployment = isLocalDeployment;
        this.basePath = basePath;
        this.cpoManager = cpoManager;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void startup(){
        if(isLocalDeployment){
            cpoManager.deleteAll();
            Set<Long> chargingStationIds = new HashSet<>();
            for(long i = 0; i < 10; i++)
                chargingStationIds.add(i);

            CpoDto cpoDto = new CpoDto(1L, basePath, chargingStationIds);
            cpoManager.createNewAndUpdate(1L, cpoDto);
        }
    }
}
