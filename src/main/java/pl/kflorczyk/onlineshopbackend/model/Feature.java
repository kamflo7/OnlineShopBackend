package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class Feature {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "feature_group_id")
    private FeatureGroup featureGroup;

    private String name;


}
