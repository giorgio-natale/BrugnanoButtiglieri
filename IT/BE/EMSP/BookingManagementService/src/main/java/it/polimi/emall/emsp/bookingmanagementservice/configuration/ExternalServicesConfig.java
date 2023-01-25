package it.polimi.emall.emsp.bookingmanagementservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.ApiClient;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.endpoints.BookingApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalServicesConfig {
    @Bean
    public BookingApi bookingApiEndPoint(
            ObjectMapper jsonObjectMapper
    ){
        ApiClient apiClient = new ApiClient(jsonObjectMapper, ApiClient.createDefaultDateFormat());
        return new BookingApi(apiClient);
    }
}
