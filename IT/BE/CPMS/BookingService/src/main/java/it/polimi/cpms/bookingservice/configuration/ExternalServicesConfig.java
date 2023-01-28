package it.polimi.cpms.bookingservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.ApiClient;
import it.polimi.emall.cpms.bookingservice.generated.http.client.cpms_bookingservice.endpoints.MockApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalServicesConfig {
    @Bean
    public MockApi mockEndPoint(
            ObjectMapper jsonObjectMapper,
            @Value("${endpoints.mock-service.base-path:#{null}}") String basePath
    ){
        ApiClient apiClient = new ApiClient(jsonObjectMapper, ApiClient.createDefaultDateFormat());
        if(basePath != null)
            apiClient.setBasePath(basePath);
        return new MockApi(apiClient);
    }
}
