package it.polimi.emall.cpms.chargingmanagementservice.controller;

import it.polimi.emall.cpms.chargingmanagementservice.model.booking.dto.BookingKafkaDto;
import it.polimi.emall.cpms.chargingmanagementservice.usecase.UpdateBookingUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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
        try {
            updateBookingUsecase.updateBooking(bookingKafkaDto);
        }catch (RuntimeException e){
            log.error("Error while processing update for booking {}: {}", bookingKafkaDto.bookingId, e.getMessage());
        } finally {
            ack.acknowledge();
        }
    }
}
