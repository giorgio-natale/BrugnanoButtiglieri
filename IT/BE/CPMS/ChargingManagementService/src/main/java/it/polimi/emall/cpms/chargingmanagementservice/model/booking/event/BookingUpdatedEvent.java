package it.polimi.emall.cpms.chargingmanagementservice.model.booking.event;


import it.polimi.emall.cpms.chargingmanagementservice.model.booking.dto.BookingKafkaDto;

public class BookingUpdatedEvent {
    public final BookingKafkaDto bookingKafkaDto;

    public BookingUpdatedEvent(BookingKafkaDto bookingKafkaDto) {
        this.bookingKafkaDto = bookingKafkaDto;
    }
}
