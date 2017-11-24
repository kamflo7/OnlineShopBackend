package pl.kflorczyk.onlineshopbackend.validators;

import java.util.Optional;

public class FeatureDefinitionValidator {

    public boolean validate(String featureDefinitionName) {
        return Optional.ofNullable(featureDefinitionName)
                .filter(c -> c.length() >= 3)
                .isPresent();
    }
}
