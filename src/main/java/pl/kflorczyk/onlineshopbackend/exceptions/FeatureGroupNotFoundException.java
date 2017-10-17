package pl.kflorczyk.onlineshopbackend.exceptions;

public class FeatureGroupNotFoundException extends RuntimeException {
    public FeatureGroupNotFoundException(String message) {
        super(message);
    }
}
