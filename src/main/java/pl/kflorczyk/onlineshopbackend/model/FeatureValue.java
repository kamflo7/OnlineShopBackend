package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FeatureValue {

    @Id
    @GeneratedValue
    private long ID;

    private String value;

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

    @Override
    public String toString() {
        return String.format("%d:%s", ID, value);
    }
}
