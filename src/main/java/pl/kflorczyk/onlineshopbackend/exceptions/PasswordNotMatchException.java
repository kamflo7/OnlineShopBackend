package pl.kflorczyk.onlineshopbackend.exceptions;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException(String msg) {
        super(msg);
    }
}
