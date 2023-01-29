package it.polimi.cpms.bookingservice.model.booking.event;

import it.polimi.cpms.bookingservice.model.booking.dto.BookingKafkaDto;

public class BookingUpdatedEvent {
    public final BookingKafkaDto bookingKafkaDto;

    public BookingUpdatedEvent(BookingKafkaDto bookingKafkaDto) {
        this.bookingKafkaDto = bookingKafkaDto;
    }
}
