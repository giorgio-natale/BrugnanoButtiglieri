package it.polimi.emall.cpms.chargingmanagementservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.dto.BookingKafkaDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;


@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic bookingTopic(@Value("${topics.booking-update}")String bookingUpdateTopic){
        return TopicBuilder
                .name(bookingUpdateTopic)
                .config(TopicConfig.RETENTION_MS_CONFIG, "600000")
                .build();
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    ConsumerFactory<String, String> defaultConsumerFactory(KafkaProperties kafkaProperties){
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
    }


    @Bean
    public ConsumerFactory<Long, BookingKafkaDto> bookingKafkaConsumerFactory(
            ConsumerFactory<String, String> defaultConsumerFactory,
            ObjectMapper jsonObjectMapper
    ){
        DefaultKafkaConsumerFactory<Long, BookingKafkaDto> consumerFactory =
                new DefaultKafkaConsumerFactory<>(defaultConsumerFactory.getConfigurationProperties());
        consumerFactory.setValueDeserializer(new JsonDeserializer<>(BookingKafkaDto.class, jsonObjectMapper, false));
        consumerFactory.setKeyDeserializer(new JsonDeserializer<>(Long.class, jsonObjectMapper, false));
        return consumerFactory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Long, BookingKafkaDto>> bookingKafkaListenerContainerFactory(
            ConsumerFactory<Long, BookingKafkaDto> bookingKafkaConsumerFactory
    ){
        ConcurrentKafkaListenerContainerFactory<Long, BookingKafkaDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookingKafkaConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
}
