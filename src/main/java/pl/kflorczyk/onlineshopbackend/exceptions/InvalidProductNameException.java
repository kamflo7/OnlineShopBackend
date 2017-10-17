package pl.kflorczyk.onlineshopbackend.exceptions;

public class InvalidProductNameException extends RuntimeException {

    public InvalidProductNameException(String message) {
        super(message);
    }
}
