package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private long ID;

    private String name;
    private BigDecimal price;
    private int amount;

    @Column(columnDefinition = "text")
    private String description;

    @OneToOne
    @JoinColumn(name = "category_logic_id")
    private CategoryLogic categoryLogic;

    @OneToMany
    @JoinColumn(name = "product_id")
    private List<FeatureValue> features;
}
