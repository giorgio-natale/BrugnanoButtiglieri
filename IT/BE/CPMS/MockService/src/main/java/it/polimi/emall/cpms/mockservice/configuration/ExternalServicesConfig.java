package it.polimi.emall.cpms.mockservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.emall.cpms.mockservice.generated.http.client.cpms_chargingmanagementservice.ApiClient;
import it.polimi.emall.cpms.mockservice.generated.http.client.cpms_chargingmanagementservice.endpoints.ChargingManagementApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalServicesConfig {

    @Bean
    public ChargingManagementApi chargingManagementEndpoint(
            ObjectMapper jsonObjectMapper,
            @Value("${endpoints.charging-management-service.base-path:#{null}}") String basePath
    ){
        ApiClient apiClient = new ApiClient(jsonObjectMapper, ApiClient.createDefaultDateFormat());
        if(basePath != null)
            apiClient.setBasePath(basePath);
        return new ChargingManagementApi(apiClient);
    }
}
