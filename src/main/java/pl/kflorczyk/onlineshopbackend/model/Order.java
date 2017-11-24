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

    @OneToOne
    @JoinColumn(name = "user_address_id")
    private UserAddress userAddress;

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

    public long getID() {
        return ID;
    }
}
