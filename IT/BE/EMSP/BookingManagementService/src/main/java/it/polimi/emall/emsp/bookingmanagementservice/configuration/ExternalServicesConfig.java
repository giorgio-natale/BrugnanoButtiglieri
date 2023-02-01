package it.polimi.emall.emsp.bookingmanagementservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.ApiClient;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.endpoints.BookingApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalServicesConfig {
    @Bean
    public BookingApi bookingApiEndPoint(
            ObjectMapper jsonObjectMapper,
            @Value("${endpoints.booking-service.base-path:#{null}}") String basePath,
            @Value("${authentication.self-token}") String selfToken
    ){
        ApiClient apiClient = new ApiClient(jsonObjectMapper, ApiClient.createDefaultDateFormat());
        apiClient.setBearerToken(selfToken);
        if(basePath != null)
            apiClient.setBasePath(basePath);
        return new BookingApi(apiClient);
    }
}
