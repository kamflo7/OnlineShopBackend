package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CategoryLogic {

    @Id
    @GeneratedValue
    private long ID;

    private String name;

    @OneToMany(mappedBy = "categoryLogic", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FeatureDefinition> featureDefinitions = new ArrayList<>();

    @OneToMany(mappedBy = "categoryLogic", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FeatureGroup> featureGroups = new ArrayList<>();

    public void addFeatureGroup(FeatureGroup featureGroup) {
        featureGroup.setCategoryLogic(this);
        this.featureGroups.add(featureGroup);
    }

    public void addFeatureDefinition(FeatureDefinition featureDefinition) {
        featureDefinition.setCategoryLogic(this);
        this.featureDefinitions.add(featureDefinition);
    }

    public CategoryLogic() {}

    public CategoryLogic(String name) {
        this.name = name;
    }

    public static CategoryLogic ofID(long id) {
        CategoryLogic categoryLogic = new CategoryLogic();
        categoryLogic.setID(id);
        return categoryLogic;
    }

    public long getID() {
        return ID;
    }

    private void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FeatureDefinition> getFeatureDefinitions() {
        return featureDefinitions;
    }

    public List<FeatureGroup> getFeatureGroups() {
        return featureGroups;
    }


}
