package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue
    private long ID;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<OrderProduct> orderProducts;

    @OneToOne
    @JoinColumn(name = "user_address_id")
    private UserAddress userAddress;
}
