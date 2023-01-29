package it.polimi.cpms.bookingservice.controller;

import it.polimi.cpms.bookingservice.model.booking.BookingStatusEnum;
import it.polimi.cpms.bookingservice.model.booking.dto.BookingKafkaDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingTypeDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.SocketTypeDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.TimeframeDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class KafkaTestController {
    private final KafkaTemplate<Long, BookingKafkaDto> bookingKafkaTemplate;

    public KafkaTestController(KafkaTemplate<Long, BookingKafkaDto> bookingKafkaTemplate) {
        this.bookingKafkaTemplate = bookingKafkaTemplate;
    }

    @GetMapping("/test/kafka")
    public void publishBooking(Long bookingId){
        bookingKafkaTemplate.send("cpms.testBooking", bookingId,
                new BookingKafkaDto(
                        bookingId,
                        "adsf",
                        5L,
                        5L,
                        5L,
                        5L,
                        SocketTypeDto.FAST,
                        new TimeframeDto(OffsetDateTime.now()),
                        BookingTypeDto.IN_ADVANCE,
                        BookingStatusEnum.BookingStatusInProgress
                ));
    }
}
