package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class UserAddress {
    @Id
    @GeneratedValue
    private long ID;

    private boolean isDefault;

    private String recipient;

    private String locality;
    private String zipCode;
    private String street;

    private String phone;

//    @ManyToOne
//    private User user;
}
