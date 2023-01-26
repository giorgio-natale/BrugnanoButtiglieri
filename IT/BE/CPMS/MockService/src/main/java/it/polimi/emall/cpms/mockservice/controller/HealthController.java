package it.polimi.emall.cpms.mockservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController{

    public ResponseEntity<Void> isReady() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
