package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderProduct {
    @Id
    @GeneratedValue
    private long ID;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal price;
    private int amount;


    public OrderProduct() {
    }

    public OrderProduct(Product product, BigDecimal price, int amount) {
        this.product = product;
        this.price = price;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
}
