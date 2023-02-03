package it.polimi.cpms.bookingservice.usecase;

import it.polimi.cpms.bookingservice.mappers.BookingMapper;
import it.polimi.cpms.bookingservice.model.booking.Booking;
import it.polimi.cpms.bookingservice.model.booking.BookingManager;
import it.polimi.cpms.bookingservice.model.booking.BookingStatusEnum;
import it.polimi.cpms.bookingservice.model.booking.dto.BookingKafkaDto;
import it.polimi.cpms.bookingservice.model.booking.event.BookingUpdatedEvent;
import it.polimi.emall.cpms.bookingservice.generated.http.client.chargingmanagementservice.endpoints.ChargingManagementApi;
import it.polimi.emall.cpms.bookingservice.generated.http.client.chargingmanagementservice.model.SocketStatusClientDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingStatusInProgressDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
        if(!booking.getBookingStatus().getBookingStatus().equals(BookingStatusEnum.BookingStatusPlanned))
            throw new IllegalStateException(String.format("Booking with code %s is not planned", booking.getBookingCode()));

        Booking updatedBooking = bookingManager.updateStatus(
                bookingManager.getEntityByKey(bookingId),
                new BookingStatusInProgressDto()
                        .bookingId(bookingId)
        );

        applicationEventPublisher.publishEvent(new BookingUpdatedEvent(BookingMapper.buildBookingKafkaDto(updatedBooking)));
    }
}
