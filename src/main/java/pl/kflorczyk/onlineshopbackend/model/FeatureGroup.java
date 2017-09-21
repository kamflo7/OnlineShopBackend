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
}
