package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Feature {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "feature_definition_id")
    private FeatureDefinition featureDefinition;

    @ManyToMany(mappedBy = "features", cascade = CascadeType.ALL)
    private List<FeatureValue> featureValues = new ArrayList<>();

    public FeatureDefinition getFeatureDefinition() {
        return featureDefinition;
    }

    public Feature() {}

    public Feature(FeatureDefinition featureDefinition, FeatureValue featureValue) {
        this.featureDefinition = featureDefinition;
        featureValues = new ArrayList<>(1);
        featureValues.add(featureValue);
    }

    public Feature(FeatureDefinition featureDefinition, List<FeatureValue> featureValues) {
        this.featureDefinition = featureDefinition;
        this.featureValues = featureValues;
    }

    public List<FeatureValue> getFeatureValues() {
        return featureValues;
    }

    public void addValue(FeatureValue value) {
        featureValues.add(value);
    }
}
