package it.polimi.cpms.bookingservice.controller;

import it.polimi.cpms.bookingservice.model.booking.dto.BookingKafkaDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaTestController {
    private final KafkaTemplate<Long, BookingKafkaDto> bookingKafkaTemplate;

    private final String bookingUpdateTopic;
    public KafkaTestController(
            KafkaTemplate<Long, BookingKafkaDto> bookingKafkaTemplate,
            @Value("${topics.booking-update}") String bookingUpdateTopic

    ) {
        this.bookingKafkaTemplate = bookingKafkaTemplate;
        this.bookingUpdateTopic = bookingUpdateTopic;
    }

    @GetMapping("/test/sendBookingUpdateOnKafka")
    public void sendBookingUpdateOnKafka(@RequestBody BookingKafkaDto bookingKafkaDto){
        bookingKafkaTemplate.send(bookingUpdateTopic, bookingKafkaDto.bookingId, bookingKafkaDto);
    }
}
