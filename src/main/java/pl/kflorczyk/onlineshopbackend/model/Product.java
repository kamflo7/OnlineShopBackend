package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_logic_id")
    @JsonIgnore
    private CategoryLogic categoryLogic;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<FeatureBag> featureBags = new ArrayList<>();

    public void addFeature(FeatureBag featureBag) throws UnsupportedOperationException {
//        if(featureBag.getFeatureDefinition().getCategoryLogic().getID() != this.categoryLogic.getID())
//            throw new UnsupportedOperationException("This featureBag does not belong to this product category");

        this.featureBags.add(featureBag);
    }

//    public void setFeatureBags(List<FeatureBag> featureBags) {
//        this.featureBags = featureBags;
//    }

    public Product() {}

    public Product(String name, BigDecimal price, int amount, String description, CategoryLogic categoryLogic) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.description = description;
        this.categoryLogic = categoryLogic;
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

    public List<FeatureBag> getFeatureBags() {
        return featureBags;
    }

    @Override
    public String toString() {
        return String.format("%d:%s", ID, name);
    }
}
