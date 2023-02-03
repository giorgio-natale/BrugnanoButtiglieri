package it.polimi.cpms.bookingservice.controller;

import it.polimi.cpms.bookingservice.model.booking.dto.SocketStatusEnum;
import it.polimi.cpms.bookingservice.model.booking.dto.SocketStatusUpdateDto;
import it.polimi.cpms.bookingservice.usecase.SynchronizeBookingUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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
        try {
            log.info("Sending socket update for booking {}: {}", socketStatusUpdateDto.bookingId, socketStatusUpdateDto.socketStatus);
            if (socketStatusUpdateDto.socketStatus.equals(SocketStatusEnum.SocketDeliveringStatus))
                synchronizeBookingUseCase.updateInProgressInformationForBooking(socketStatusUpdateDto);
            else if (socketStatusUpdateDto.socketStatus.equals(SocketStatusEnum.SocketStoppedStatus)) {
                synchronizeBookingUseCase.closeBooking(
                        socketStatusUpdateDto.bookingId
                );
            }
        }catch (RuntimeException e){
            log.error("Error while processing socket update for booking {}: {}", socketStatusUpdateDto.bookingId, e.getMessage());
        } finally {
            acknowledgment.acknowledge();
        }

    }
}
