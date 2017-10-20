package pl.kflorczyk.onlineshopbackend.exceptions;

public class InvalidAddressException extends RuntimeException {
    public InvalidAddressException(String s) {
        super(s);
    }
}
