package it.polimi.emall.cpms.chargingmanagementservice.controller;

import it.polimi.emall.cpms.chargingmanagementservice.model.booking.dto.BookingKafkaDto;
import it.polimi.emall.cpms.chargingmanagementservice.usecase.UpdateBookingUsecase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaController {
    private final UpdateBookingUsecase updateBookingUsecase;

    public KafkaController(UpdateBookingUsecase updateBookingUsecase) {
        this.updateBookingUsecase = updateBookingUsecase;
    }

    @KafkaListener(
            containerFactory = "bookingKafkaListenerContainerFactory",
            autoStartup = "true",
            topics = "${topics.booking-update}"
    )
    public void updateBooking(BookingKafkaDto bookingKafkaDto, Acknowledgment ack){
        updateBookingUsecase.updateBooking(bookingKafkaDto);
        ack.acknowledge();
    }
}
