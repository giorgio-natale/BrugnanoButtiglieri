package it.polimi.cpms.bookingservice.model.booking;

public class NoAvailableSocketException extends RuntimeException{
    public NoAvailableSocketException(String message) {
        super(message);
    }
}
