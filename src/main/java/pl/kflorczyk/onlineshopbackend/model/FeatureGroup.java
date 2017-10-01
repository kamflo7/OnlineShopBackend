package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class FeatureGroup {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "category_logic_id")
    private CategoryLogic categoryLogic;

    private String name;

    public CategoryLogic getCategoryLogic() {
        return categoryLogic;
    }

    public void setCategoryLogic(CategoryLogic categoryLogic) {
        this.categoryLogic = categoryLogic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
