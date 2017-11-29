package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonFilter("FeatureBag")
public class FeatureBag {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "feature_definition_id")
    private FeatureDefinition featureDefinition;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<FeatureValue> featureValues = new ArrayList<>();

    public FeatureDefinition getFeatureDefinition() {
        return featureDefinition;
    }

    public FeatureBag() {}

    public FeatureBag(FeatureDefinition featureDefinition) {
        this.featureDefinition = featureDefinition;
    }

    public FeatureBag(FeatureDefinition featureDefinition, FeatureValue featureValue) {
        this.featureDefinition = featureDefinition;
        featureValues = new ArrayList<>(1);
        featureValues.add(featureValue);
    }

    public FeatureBag(FeatureDefinition featureDefinition, List<FeatureValue> featureValues) {
        this.featureDefinition = featureDefinition;
        this.featureValues = featureValues;
    }

    public List<FeatureValue> getFeatureValues() {
        return featureValues;
    }

    public void addValue(FeatureValue value) {
        featureValues.add(value);
    }

    public void setFeatureValues(List<FeatureValue> featureValues) {
        this.featureValues = featureValues;
    }

    public long getId() {
        return id;
    }
}
