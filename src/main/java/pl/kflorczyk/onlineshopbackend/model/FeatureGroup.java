package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class FeatureGroup {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_logic_id")
    @JsonBackReference
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

    @Override
    public String toString() {
        return String.format("%d:%s", id, name);
    }

    public void setId(long id) {
        this.id = id;
    }
}
