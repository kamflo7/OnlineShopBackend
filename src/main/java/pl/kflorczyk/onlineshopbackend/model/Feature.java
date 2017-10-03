package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class Feature {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "feature_definition_id")
    private FeatureDefinition featureDefinition;

    private String value;

    public FeatureDefinition getFeatureDefinition() {
        return featureDefinition;
    }

    public Feature() {}

    public Feature(FeatureDefinition featureDefinition, String value) {
        this.featureDefinition = featureDefinition;
        this.value = value;
    }
}
