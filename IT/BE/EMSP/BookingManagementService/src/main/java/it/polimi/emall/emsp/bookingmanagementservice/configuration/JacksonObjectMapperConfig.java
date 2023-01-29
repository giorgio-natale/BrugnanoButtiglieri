package it.polimi.emall.emsp.bookingmanagementservice.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.ConstructorDetector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class JacksonObjectMapperConfig {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Primary
    public ObjectMapper jsonObjectMapper(){
        ObjectMapper mapper = JsonMapper.builder()
                .constructorDetector(ConstructorDetector.USE_PROPERTIES_BASED)
                .build();
        mapper.findAndRegisterModules();
        mapper.registerModule( new ParameterNamesModule())
                .enable( DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS )
                .enable( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES )
                .enable( DeserializationFeature.WRAP_EXCEPTIONS )
                .enable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));

        return mapper;
    }
}