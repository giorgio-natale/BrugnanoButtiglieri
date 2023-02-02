package it.polimi.emall.cpms.chargingmanagementservice.processes;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import it.polimi.emall.cpms.chargingmanagementservice.generated.http.client.cpms_mockingservice.endpoints.CpmsChargingStationConfigurationApi;
import it.polimi.emall.cpms.chargingmanagementservice.generated.http.client.cpms_mockingservice.model.ChargingStationClientDto;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatusDto;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatusManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
@Slf4j
public class StartupProcesses {

    private final CpmsChargingStationConfigurationApi mockApi;
    private final SocketCurrentStatusManager socketCurrentStatusManager;
    private final TransactionTemplate transactionTemplate;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    private final RetryRegistry retryRegistry;


    public StartupProcesses(
            CpmsChargingStationConfigurationApi mockApi,
            SocketCurrentStatusManager socketCurrentStatusManager,
            PlatformTransactionManager platformTransactionManager,
            CircuitBreakerRegistry circuitBreakerRegistry, RetryRegistry retryRegistry) {
        this.mockApi = mockApi;
        this.socketCurrentStatusManager = socketCurrentStatusManager;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.retryRegistry = retryRegistry;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void startup(){
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("externalService", "externalService");
        Retry retry = retryRegistry.retry("externalService", "externalService");
        List<ChargingStationClientDto> chargingStationClientDtoList =
        Retry.decorateSupplier(retry, CircuitBreaker.decorateSupplier(
                    circuitBreaker, () -> {
                        try {
                            return mockApi.getChargingStationConfigurationList().collectList().block();
                        }catch (RuntimeException e){
                            log.error("Cannot retrieve charging stations information: {}", e.getMessage());
                            throw e;
                        }
                })
        ).get();
        assert chargingStationClientDtoList != null;
        transactionTemplate.execute(status -> {
            chargingStationClientDtoList.forEach(chargingStationClientDto ->
                    chargingStationClientDto.getChargingPointList().forEach(
                            chargingPointClientDto ->
                                    chargingPointClientDto.getSocketList().forEach(socketClientDto -> {
                                        if (socketCurrentStatusManager.getByIdOpt(socketClientDto.getSocketId()).isEmpty()) {
                                            socketCurrentStatusManager.createNewAndUpdate(
                                                    socketClientDto.getSocketId(),
                                                    new SocketCurrentStatusDto(
                                                            socketClientDto.getSocketId(),
                                                            chargingPointClientDto.getChargingPointId(),
                                                            chargingStationClientDto.getChargingStationId(),
                                                            SocketStatusEnum.SocketAvailableStatus,
                                                            null
                                                    ));
                                        }
                                    }))
                    );
            return null;
        });

    }
}
