package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class FeatureValue {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "feature_id")
    private Feature feature;

    private String value;
}
