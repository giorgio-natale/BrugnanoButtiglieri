package it.polimi.cpms.bookingservice.processes;

import it.polimi.cpms.bookingservice.model.booking.dto.BookingKafkaDto;
import it.polimi.cpms.bookingservice.model.booking.event.BookingUpdatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaBookingUpdateProducerListener {
    private final KafkaTemplate<Long, BookingKafkaDto> bookingKafkaTemplate;
    private final String bookingUpdateTopic;

    public KafkaBookingUpdateProducerListener(
            KafkaTemplate<Long, BookingKafkaDto> bookingKafkaTemplate,
            @Value("${topics.booking-update}") String bookingUpdateTopic
    ) {
        this.bookingKafkaTemplate = bookingKafkaTemplate;
        this.bookingUpdateTopic = bookingUpdateTopic;
    }

    @EventListener(BookingUpdatedEvent.class)
    public void sendBookingUpdatedEventOnKafka(BookingUpdatedEvent bookingUpdatedEvent){
        BookingKafkaDto bookingKafkaDto = bookingUpdatedEvent.bookingKafkaDto;
        bookingKafkaTemplate.send(bookingUpdateTopic, bookingKafkaDto.bookingId, bookingKafkaDto);
    }
}
