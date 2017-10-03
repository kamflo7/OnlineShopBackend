package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class FeatureGroup {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_logic_id")
    private CategoryLogic categoryLogic;

    public FeatureGroup() { }

    public FeatureGroup(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryLogic(CategoryLogic categoryLogic) {
        this.categoryLogic = categoryLogic;
    }
}
