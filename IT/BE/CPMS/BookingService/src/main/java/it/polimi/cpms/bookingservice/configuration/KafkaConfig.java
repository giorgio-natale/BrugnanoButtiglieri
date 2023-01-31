package it.polimi.cpms.bookingservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.cpms.bookingservice.model.booking.dto.BookingKafkaDto;
import it.polimi.cpms.bookingservice.model.booking.dto.SocketStatusUpdateDto;
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
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic bookingTopic(@Value("${topics.booking-update}") String topic){
        return TopicBuilder
                .name(topic)
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


    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    ConsumerFactory<String, String> defaultConsumerFactory(KafkaProperties kafkaProperties){
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
    }


    @Bean
    public ConsumerFactory<Long, SocketStatusUpdateDto> socketStatusUpdateKafkaConsumerFactory(
            ConsumerFactory<String, String> defaultConsumerFactory,
            ObjectMapper jsonObjectMapper
    ){
        DefaultKafkaConsumerFactory<Long, SocketStatusUpdateDto> consumerFactory =
                new DefaultKafkaConsumerFactory<>(defaultConsumerFactory.getConfigurationProperties());
        consumerFactory.setValueDeserializer(new JsonDeserializer<>(SocketStatusUpdateDto.class, jsonObjectMapper, false));
        consumerFactory.setKeyDeserializer(new JsonDeserializer<>(Long.class, jsonObjectMapper, false));
        return consumerFactory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Long, SocketStatusUpdateDto>> socketStatusUpdateKafkaListenerContainerFactory(
            ConsumerFactory<Long, SocketStatusUpdateDto> socketStatusUpdateKafkaConsumerFactory
    ){
        ConcurrentKafkaListenerContainerFactory<Long, SocketStatusUpdateDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(socketStatusUpdateKafkaConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
}
