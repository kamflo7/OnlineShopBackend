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

    @ManyToOne
    @JoinColumn(name = "category_logic_id")
    private CategoryLogic categoryLogic;

    @OneToMany
    @JoinColumn(name = "product_id")
    private List<FeatureValue> features;

    public Product() {}

    public Product(String name, BigDecimal price, int amount, String description) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryLogic getCategoryLogic() {
        return categoryLogic;
    }

    public void setCategoryLogic(CategoryLogic categoryLogic) {
        this.categoryLogic = categoryLogic;
    }

    public List<FeatureValue> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureValue> features) {
        this.features = features;
    }
}
