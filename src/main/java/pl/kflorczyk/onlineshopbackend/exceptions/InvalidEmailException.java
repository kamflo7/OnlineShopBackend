package pl.kflorczyk.onlineshopbackend.exceptions;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String msg) {
        super(msg);
    }
}
