package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class CategoryView {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private CategoryView parent;

    @OneToOne
    @JoinColumn(name = "category_logic_id")
    private CategoryLogic categoryLogic;

    @OneToMany
    @JoinColumn(name = "category_view_id")
    private List<CategoryViewFilter> filters;
}
