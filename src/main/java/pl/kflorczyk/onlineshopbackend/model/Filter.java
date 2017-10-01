package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Filter {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private FeatureDefinition featureDefinition;

    private String customType;
    private String customExpression;
    // todo: moze jakis enum na powyzsze?
}
