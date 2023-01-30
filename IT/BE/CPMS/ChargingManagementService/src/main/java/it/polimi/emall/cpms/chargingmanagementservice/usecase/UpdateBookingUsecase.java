package it.polimi.emall.cpms.chargingmanagementservice.usecase;

import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.dto.BookingKafkaDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateBookingUsecase {

    private final BookingManager bookingManager;

    public UpdateBookingUsecase(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

    @Transactional
    public void updateBooking(BookingKafkaDto bookingKafkaDto){
        bookingManager.getOrCreateNewAndUpdate(bookingKafkaDto.bookingId, bookingKafkaDto);

    }
}
