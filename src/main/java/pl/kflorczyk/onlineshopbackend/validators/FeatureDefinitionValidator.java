package pl.kflorczyk.onlineshopbackend.validators;

public class FeatureDefinitionValidator {

    public boolean validate(String featureDefinitionName) {
        if(featureDefinitionName != null && featureDefinitionName.length() > 3) {
            return true;
        }
        return false;
    }
}
