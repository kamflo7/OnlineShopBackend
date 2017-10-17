package pl.kflorczyk.onlineshopbackend.exceptions;

public class InvalidFeatureGroupNameException extends RuntimeException {
    public InvalidFeatureGroupNameException(String msg) {
        super(msg);
    }
}
