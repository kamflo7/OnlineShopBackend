package pl.kflorczyk.onlineshopbackend.exceptions;

public class PasswordNotMatchException extends Exception {
    public PasswordNotMatchException(String msg) {
        super(msg);
    }
}
