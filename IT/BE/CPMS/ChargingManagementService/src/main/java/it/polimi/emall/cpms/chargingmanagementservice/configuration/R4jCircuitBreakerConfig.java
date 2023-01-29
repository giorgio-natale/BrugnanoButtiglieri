package it.polimi.emall.cpms.chargingmanagementservice.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

@Configuration
public class R4jCircuitBreakerConfig {
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig externalServiceFooConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .slidingWindowType(COUNT_BASED)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50.0f)
                .build();
        return CircuitBreakerRegistry.of(
                Map.of("externalService", externalServiceFooConfig)
        );
    }

    @Bean
    public RetryRegistry retryRegistry(){
        RetryConfig config = RetryConfig
                .custom()
                .maxAttempts(100)
                .waitDuration(Duration.of(2, ChronoUnit.SECONDS))
                .build();
        return RetryRegistry.of(
                Map.of("externalService", config)
        );
    }
}
