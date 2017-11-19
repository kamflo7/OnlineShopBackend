package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@JsonFilter("Order")
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue
    private long ID;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderProduct> orderProducts = new ArrayList<>();

//    @Formula(value = "SELECT SUM(OrderProduct.price*OrderProduct.amount) WHERE OrderProduct.order_id = ID")
//    private BigDecimal testTotalSum;

    @OneToOne
    @JoinColumn(name = "user_address_id")
    private UserAddress userAddress;

    // don't have time to over complicate simple learning project
    private String deliveryMethod;
    private String paymentMethod;

    public Order() {
    }

    public Order(Date date, User user, UserAddress userAddress, String deliveryMethod, String paymentMethod) {
        this.date = date;
        this.user = user;
        this.userAddress = userAddress;
        this.deliveryMethod = deliveryMethod;
        this.paymentMethod = paymentMethod;
    }

    public void addOrderProducts(List<OrderProduct> orderProducts) {
        for(OrderProduct o : orderProducts)
            addOrderProduct(o);
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
