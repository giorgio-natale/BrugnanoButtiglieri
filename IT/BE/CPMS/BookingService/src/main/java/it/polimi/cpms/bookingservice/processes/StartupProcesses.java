package it.polimi.cpms.bookingservice.processes;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import it.polimi.cpms.bookingservice.model.chargingstation.ChargingStationManager;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.endpoints.CpmsChargingStationConfigurationApi;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.model.ChargingStationClientDto;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
public class StartupProcesses {

    private final CpmsChargingStationConfigurationApi mockApi;
    private final ChargingStationManager chargingStationManager;
    private final TransactionTemplate transactionTemplate;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    private final RetryRegistry retryRegistry;


    public StartupProcesses(
            CpmsChargingStationConfigurationApi mockApi,
            ChargingStationManager chargingStationManager,
            PlatformTransactionManager platformTransactionManager,
            CircuitBreakerRegistry circuitBreakerRegistry, RetryRegistry retryRegistry) {
        this.mockApi = mockApi;
        this.chargingStationManager = chargingStationManager;
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
                    circuitBreaker, () -> mockApi.getChargingStationConfigurationList().collectList().block()
                )
        ).get();
        assert chargingStationClientDtoList != null;
        transactionTemplate.execute(status -> {
            chargingStationClientDtoList.forEach(chargingStationClientDto -> chargingStationManager.getOrCreateNewAndUpdate(
                    chargingStationClientDto.getChargingStationId(),
                    chargingStationClientDto
            ));
            return null;
        });

    }
}
