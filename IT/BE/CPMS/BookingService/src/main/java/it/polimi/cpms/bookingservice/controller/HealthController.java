package it.polimi.cpms.bookingservice.controller;

import it.polimi.emall.cpms.bookingservice.generated.http.server.controller.HealthApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthApi {
    @Override
    public ResponseEntity<Void> isReady() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
