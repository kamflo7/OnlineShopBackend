package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;
import java.math.BigDecimal;

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
    @JoinColumn(name = "category_id")
    private Category category;
}
