package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FeatureValue {

    @Id
    @GeneratedValue
    private long ID;

    private String value;

//    @ManyToMany(cascade = CascadeType.ALL)
//    private List<Feature> features = new ArrayList<>(); // todo: remove this or something; It was NOT planned
    // but I did this, because I thought it is impossible to do ManyToMany relationship without bidirectional

    public FeatureValue() {}

    public FeatureValue(String value) {
        this.value = value;
    }

    public static FeatureValue ofID(long id) {
        FeatureValue featureValue = new FeatureValue();
        featureValue.setID(id);
        return featureValue;
    }

    public long getID() {
        return ID;
    }

    private void setID(long ID) {
        this.ID = ID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
