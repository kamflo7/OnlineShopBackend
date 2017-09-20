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

//    @OneToOne
//    private User user;

    private Date date;

    @ManyToOne
    private User user;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<OrderProduct> orderProducts;

}
