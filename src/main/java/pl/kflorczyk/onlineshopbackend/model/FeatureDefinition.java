package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class FeatureDefinition {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "feature_group_id")
    private FeatureGroup featureGroup;

    private String name;


}
