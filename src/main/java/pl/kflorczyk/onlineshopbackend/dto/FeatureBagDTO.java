package pl.kflorczyk.onlineshopbackend.dto;

import pl.kflorczyk.onlineshopbackend.model.FeatureDefinition;
import pl.kflorczyk.onlineshopbackend.model.FeatureValue;

import java.util.ArrayList;
import java.util.List;

public class FeatureBagDTO {

    private FeatureDefinition featureDefinition;
    private List<FeatureValue> featureValues;

    public FeatureBagDTO(FeatureDefinition featureDefinition) {
        this.featureDefinition = featureDefinition;
        this.featureValues = new ArrayList<>();
    }

    public FeatureBagDTO(FeatureDefinition featureDefinition, List<FeatureValue> featureValues) {
        this.featureDefinition = featureDefinition;
        this.featureValues = featureValues;
    }

    public FeatureDefinition getFeatureDefinition() {
        return featureDefinition;
    }

    public void setFeatureDefinition(FeatureDefinition featureDefinition) {
        this.featureDefinition = featureDefinition;
    }

    public List<FeatureValue> getFeatureValues() {
        return featureValues;
    }

    public void setFeatureValues(List<FeatureValue> featureValues) {
        this.featureValues = featureValues;
    }

    public void addFeatureValue(FeatureValue featureValue) {
        this.featureValues.add(featureValue);
    }
}
