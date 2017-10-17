package pl.kflorczyk.onlineshopbackend.exceptions;

public class InvalidProductDescriptionException extends RuntimeException {

    public InvalidProductDescriptionException(String message) {
        super(message);
    }
}
