package pl.kflorczyk.onlineshopbackend.validators;

import java.util.Optional;

public class FeatureGroupValidator {

    public boolean validate(String featureGroupName) {
        return Optional.ofNullable(featureGroupName)
                .filter(c -> c.length() >= 3)
                .isPresent();
    }
}
