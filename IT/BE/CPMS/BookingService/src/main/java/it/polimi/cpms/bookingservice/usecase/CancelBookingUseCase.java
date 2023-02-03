package it.polimi.cpms.bookingservice.usecase;

import it.polimi.cpms.bookingservice.mappers.BookingMapper;
import it.polimi.cpms.bookingservice.model.booking.Booking;
import it.polimi.cpms.bookingservice.model.booking.BookingManager;
import it.polimi.cpms.bookingservice.model.booking.BookingStatusEnum;
import it.polimi.cpms.bookingservice.model.booking.event.BookingUpdatedEvent;
import it.polimi.emall.cpms.bookingservice.generated.http.client.chargingmanagementservice.endpoints.ChargingManagementApi;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingStatusCancelledDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CancelBookingUseCase {
    private final BookingManager bookingManager;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ChargingManagementApi chargingManagementApi;


    public CancelBookingUseCase(BookingManager bookingManager, ApplicationEventPublisher applicationEventPublisher, ChargingManagementApi chargingManagementApi) {
        this.bookingManager = bookingManager;
        this.applicationEventPublisher = applicationEventPublisher;
        this.chargingManagementApi = chargingManagementApi;
    }

    @Transactional
    public void cancelBooking(Long bookingId){
        Booking booking = bookingManager.getEntityByKey(bookingId);
        if(!booking.getBookingStatus().getBookingStatus().equals(BookingStatusEnum.BookingStatusPlanned)) {
            log.error("Booking with code {} is not planned", booking.getBookingCode());
            throw new IllegalStateException(String.format("Booking with code %s is not planned", booking.getBookingCode()));
        }

        Booking updatedBooking = bookingManager.updateStatus(
                bookingManager.getEntityByKey(bookingId),
                new BookingStatusCancelledDto()
                        .bookingId(bookingId)
        );

        applicationEventPublisher.publishEvent(new BookingUpdatedEvent(BookingMapper.buildBookingKafkaDto(updatedBooking)));
    }
}
