package it.polimi.emall.cpms.chargingmanagementservice.controller;

import it.polimi.emall.cpms.chargingmanagementservice.model.booking.dto.BookingKafkaDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaController {

    @KafkaListener(
            containerFactory = "bookingKafkaListenerContainerFactory",
            autoStartup = "true",
            topics = "${topics.booking-update}"
    )
    public void updateBooking(BookingKafkaDto bookingKafkaDto, Acknowledgment ack){
            System.out.printf("RECEIVED BOOKING UPDATE. ID:%d STATUS:%s\n", bookingKafkaDto.bookingId, bookingKafkaDto.status);
            ack.acknowledge();
    }
}
