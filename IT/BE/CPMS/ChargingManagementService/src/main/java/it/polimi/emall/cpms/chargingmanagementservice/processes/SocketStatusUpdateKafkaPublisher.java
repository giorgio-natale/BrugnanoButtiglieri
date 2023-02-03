package it.polimi.emall.cpms.chargingmanagementservice.processes;

import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.dto.SocketStatusUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketStatusUpdateKafkaPublisher {
    private final KafkaTemplate<Long, SocketStatusUpdateDto> socketStatusUpdateKafkaTemplate;
    private final String topic;

    public SocketStatusUpdateKafkaPublisher(
            KafkaTemplate<Long, SocketStatusUpdateDto> socketStatusUpdateKafkaTemplate,
            @Value("${topics.socket-update}") String topic
    ) {
        this.socketStatusUpdateKafkaTemplate = socketStatusUpdateKafkaTemplate;
        this.topic = topic;
    }

    @EventListener(SocketStatusUpdateDto.class)
    public void sendSocketStatusUpdate(SocketStatusUpdateDto socketStatusUpdateDto){
        log.info("Sending socket update for booking {}: {}", socketStatusUpdateDto.bookingId, socketStatusUpdateDto.socketStatus);
        socketStatusUpdateKafkaTemplate.send(topic, socketStatusUpdateDto.bookingId, socketStatusUpdateDto);
    }
}
