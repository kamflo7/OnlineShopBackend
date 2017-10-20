package pl.kflorczyk.onlineshopbackend.exceptions;

public class FeatureDefinitionAlreadyExists extends RuntimeException {
    public FeatureDefinitionAlreadyExists(String s) {
        super(s);
    }
}
