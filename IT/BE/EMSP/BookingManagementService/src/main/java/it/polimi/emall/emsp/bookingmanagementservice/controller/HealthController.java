package it.polimi.emall.emsp.bookingmanagementservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.controller.HealthApi;

@RestController
public class HealthController implements HealthApi {
    @Override
    public ResponseEntity<Void> isReady() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
