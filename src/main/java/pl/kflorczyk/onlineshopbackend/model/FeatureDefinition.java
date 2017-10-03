package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class FeatureDefinition {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_group_id")
    private FeatureGroup featureGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_logic_id")
    private CategoryLogic categoryLogic;

    private String name;

    public FeatureDefinition() {}

    public FeatureDefinition(String name, FeatureGroup featureGroup) {
        this.name = name;
        this.featureGroup = featureGroup;
    }

    public FeatureGroup getFeatureGroup() {
        return featureGroup;
    }

    public long getId() {
        return id;
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
}
