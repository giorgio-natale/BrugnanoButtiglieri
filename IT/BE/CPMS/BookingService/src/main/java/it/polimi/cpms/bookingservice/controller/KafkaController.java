package it.polimi.cpms.bookingservice.controller;

import it.polimi.cpms.bookingservice.model.booking.dto.SocketStatusEnum;
import it.polimi.cpms.bookingservice.model.booking.dto.SocketStatusUpdateDto;
import it.polimi.cpms.bookingservice.usecase.SynchronizeBookingUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaController {
    private final SynchronizeBookingUseCase synchronizeBookingUseCase;

    public KafkaController(SynchronizeBookingUseCase synchronizeBookingUseCase) {
        this.synchronizeBookingUseCase = synchronizeBookingUseCase;
    }

    @KafkaListener(
            containerFactory = "socketStatusUpdateKafkaListenerContainerFactory",
            autoStartup = "true",
            topics = "${topics.socket-update}"
    )
    public void handleSocketUpdate(SocketStatusUpdateDto socketStatusUpdateDto, Acknowledgment acknowledgment){
        if(socketStatusUpdateDto.socketStatus.equals(SocketStatusEnum.SocketDeliveringStatus))
            synchronizeBookingUseCase.updateInProgressInformationForBooking(socketStatusUpdateDto);
        else if(socketStatusUpdateDto.socketStatus.equals(SocketStatusEnum.SocketStoppedStatus)){
            synchronizeBookingUseCase.closeBooking(
                    socketStatusUpdateDto.bookingId
            );
        }

        acknowledgment.acknowledge();
    }
}
