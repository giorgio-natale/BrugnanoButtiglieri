package it.polimi.cpms.bookingservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.cpms.bookingservice.model.booking.dto.BookingKafkaDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic bookingTopic(){
        return TopicBuilder
                .name("cpms.testBooking")
                .config(TopicConfig.RETENTION_MS_CONFIG, "600000")
                .build();
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    ProducerFactory<String, String> defaultProducerFactory(KafkaProperties kafkaProperties){
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    }

    @Bean
    public ProducerFactory<Long, BookingKafkaDto> bookingKafkaProducerFactory(
            ProducerFactory<String, String> defaultProducerFactory,
            ObjectMapper jsonObjectMapper
    ) {
        DefaultKafkaProducerFactory<Long, BookingKafkaDto> producerFactory =
                new DefaultKafkaProducerFactory<>(defaultProducerFactory.getConfigurationProperties());

        producerFactory.setValueSerializer(new JsonSerializer<>(jsonObjectMapper));
        producerFactory.setKeySerializer(new JsonSerializer<>(jsonObjectMapper));
        return producerFactory;
    }

    @Bean
    public KafkaTemplate<Long, BookingKafkaDto> bookingKafkaTemplate(
            ProducerFactory<Long, BookingKafkaDto> bookingKafkaProducerFactory
    ){
        return new KafkaTemplate<>(bookingKafkaProducerFactory);
    }
}
