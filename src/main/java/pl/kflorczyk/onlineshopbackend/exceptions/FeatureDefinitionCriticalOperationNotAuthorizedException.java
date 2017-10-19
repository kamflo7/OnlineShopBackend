package pl.kflorczyk.onlineshopbackend.exceptions;

public class FeatureDefinitionCriticalOperationNotAuthorizedException extends RuntimeException {
    public FeatureDefinitionCriticalOperationNotAuthorizedException(String s) {
        super(s);
    }
}
