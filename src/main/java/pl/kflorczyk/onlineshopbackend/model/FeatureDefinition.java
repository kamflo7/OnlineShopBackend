package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonFilter("getProductsFilter_featureDefinition")
public class FeatureDefinition {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // todo: for sure it has appropriate cascade type?
    @JoinColumn(name = "feature_group_id")
    private FeatureGroup featureGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_logic_id")
    @JsonBackReference
    private CategoryLogic categoryLogic;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_definition_id")
    private List<FeatureValue> featureValueDefinitions = new ArrayList<>();

    private boolean multipleValues;

    private String name;

    private boolean filterable;

    private boolean visible;

    // not the best ofc, but don't have enough time to redesign FilterParameters logic, so this is temporary solution
    // todo: dummy solution, don't have enough time, fix later
    @Transient
    @JsonIgnore
    private boolean dummyPriceFilter;

    public FeatureDefinition() {}

    public FeatureDefinition(String name, FeatureGroup featureGroup) {
        this(name, featureGroup, false);
    }

    public FeatureDefinition(String name, FeatureGroup featureGroup, boolean filterable) {
        this(name, featureGroup, filterable, false);
    }

    public FeatureDefinition(String name, FeatureGroup featureGroup, boolean filterable, boolean multipleValues) {
        this(name, featureGroup, filterable, multipleValues, true);
    }

    public FeatureDefinition(String name, FeatureGroup featureGroup, boolean filterable, boolean multipleValues, boolean visible) {
        this.name = name;
        this.featureGroup = featureGroup;
        this.filterable = filterable;
        this.multipleValues = multipleValues;
        this.visible = visible;
    }

    public static FeatureDefinition forPriceFilter() {
        FeatureDefinition fd = new FeatureDefinition();
        fd.dummyPriceFilter = true;
        return fd;
    }

    public boolean isDummyPriceFilter() {
        return dummyPriceFilter;
    }

    public static FeatureDefinition ofID(long id) {
        FeatureDefinition featureDefinition = new FeatureDefinition();
        featureDefinition.setId(id);
        return featureDefinition;
    }

    public boolean isMultipleValues() {
        return multipleValues;
    }

    public FeatureGroup getFeatureGroup() {
        return featureGroup;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setCategoryLogic(CategoryLogic categoryLogic) {
        this.categoryLogic = categoryLogic;
    }

    public CategoryLogic getCategoryLogic() {
        return categoryLogic;
    }

    public boolean isFilterable() {
        return filterable;
    }

    public void setFilterable(boolean filterable) {
        this.filterable = filterable;
    }

    public List<FeatureValue> getFeatureValueDefinitions() {
        return featureValueDefinitions;
    }

    public void setFeatureValueDefinitions(List<FeatureValue> featureValueDefinitions) {
        this.featureValueDefinitions = featureValueDefinitions;
    }

    @Override
    public String toString() {
        return String.format("%d:%s", id, name);
    }

    public boolean isVisible() {
        return visible;
    }
}
