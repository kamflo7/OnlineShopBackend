package pl.kflorczyk.onlineshopbackend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ProductDTO {
    private String name, description;
    private BigDecimal price;
    private int amount;

    private Map<Long, List<Long>> features;

    public ProductDTO() { }

    public ProductDTO(String name, String description, BigDecimal price, int amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Map<Long, List<Long>> getFeatures() {
        return features;
    }

    public void setFeatures(Map<Long, List<Long>> features) {
        this.features = features;
    }
}
