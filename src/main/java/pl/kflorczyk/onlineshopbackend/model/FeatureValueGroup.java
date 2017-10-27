package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FeatureValueGroup {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<FeatureValue> values = new ArrayList<>();

    public FeatureValueGroup() {
    }

    public FeatureValueGroup(List<FeatureValue> values) {
        this.values = values;
    }

    public List<FeatureValue> getValues() {
        return values;
    }

    public void setValues(List<FeatureValue> values) {
        this.values = values;
    }
}
