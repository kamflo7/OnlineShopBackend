package pl.kflorczyk.onlineshopbackend.validators;

public class FeatureGroupValidator {

    public boolean validate(String featureGroupName) {
        if(featureGroupName != null && featureGroupName.length() > 3) {
            return true;
        }
        return false;
    }
}
