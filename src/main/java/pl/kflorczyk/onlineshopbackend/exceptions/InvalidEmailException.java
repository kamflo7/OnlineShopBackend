package pl.kflorczyk.onlineshopbackend.exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String msg) {
        super(msg);
    }
}
