package it.polimi.emall.emsp.bookingmanagementservice.usecases;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoAvailableSocketException extends RuntimeException{
    public NoAvailableSocketException(String message) {
        super(message);
    }
}
