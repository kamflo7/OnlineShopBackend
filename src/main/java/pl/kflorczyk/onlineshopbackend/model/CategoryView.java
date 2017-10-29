package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@JsonFilter("CategoryView")
public class CategoryView {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties({"parent", "categoryLogic", "filters"})
    private CategoryView parent;

    @OneToOne
    @JoinColumn(name = "category_logic_id")
    private CategoryLogic categoryLogic;

//    @OneToMany(cascade = CascadeType.ALL)
//    private Map<FeatureDefinition, FeatureValueGroup> filters = new HashMap<>();


    public CategoryView() { }

    public CategoryView(String name) {
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

    public CategoryView getParent() {
        return parent;
    }

    public void setParent(CategoryView parent) {
        this.parent = parent;
    }

    public CategoryLogic getCategoryLogic() {
        return categoryLogic;
    }

    public void setCategoryLogic(CategoryLogic categoryLogic) {
        this.categoryLogic = categoryLogic;
    }
}