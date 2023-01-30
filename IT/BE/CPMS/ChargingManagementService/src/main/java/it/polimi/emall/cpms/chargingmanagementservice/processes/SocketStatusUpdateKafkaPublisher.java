package it.polimi.emall.cpms.chargingmanagementservice.processes;

import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.dto.SocketStatusUpdateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SocketStatusUpdateKafkaPublisher {
    private final KafkaTemplate<Long, SocketStatusUpdateDto> socketStatusUpdateKafkaTemplate;
    private final String topic;

    public SocketStatusUpdateKafkaPublisher(
            KafkaTemplate<Long, SocketStatusUpdateDto> socketStatusUpdateKafkaTemplate,
            @Value("topics.socket-status-update") String topic
    ) {
        this.socketStatusUpdateKafkaTemplate = socketStatusUpdateKafkaTemplate;
        this.topic = topic;
    }

    @EventListener(SocketStatusUpdateDto.class)
    public void sendSocketStatusUpdate(SocketStatusUpdateDto socketStatusUpdateDto){
        socketStatusUpdateKafkaTemplate.send(topic, socketStatusUpdateDto.bookingId, socketStatusUpdateDto);
    }
}
