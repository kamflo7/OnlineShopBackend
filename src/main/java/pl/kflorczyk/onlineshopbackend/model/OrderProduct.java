package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderProduct {
    @Id
    @GeneratedValue
    private long ID;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal price;
    private int amount;

    //todo: statu
}
