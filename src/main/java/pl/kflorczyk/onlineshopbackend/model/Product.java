package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonFilter("Product")
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
    private CategoryLogic categoryLogic;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<FeatureBag> featureBags = new ArrayList<>();

    @OneToOne
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void addFeature(FeatureBag featureBag) {
        this.featureBags.add(featureBag);
    }

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

    public long getID() {
        return ID;
    }
}
