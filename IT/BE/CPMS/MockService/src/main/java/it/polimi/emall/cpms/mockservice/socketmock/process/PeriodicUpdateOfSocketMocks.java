package it.polimi.emall.cpms.mockservice.socketmock.process;

import it.polimi.emall.cpms.mockservice.socketmock.usecase.SimulateChargingPointUseCase;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class PeriodicUpdateOfSocketMocks {
    private final SimulateChargingPointUseCase simulateChargingPointUseCase;

    public PeriodicUpdateOfSocketMocks(SimulateChargingPointUseCase simulateChargingPointUseCase) {
        this.simulateChargingPointUseCase = simulateChargingPointUseCase;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startup(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    simulateChargingPointUseCase.simulateChargingPoints();
                }catch (RuntimeException e){
                    e.printStackTrace();
                }
            }
        }, 1000, 3000);
    }
}
