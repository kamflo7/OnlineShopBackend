package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class CategoryViewFilter {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "feature_id")
    private FeatureDefinition featureDefinition;

    private String value;
}
