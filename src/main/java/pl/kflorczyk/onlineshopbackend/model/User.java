package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonFilter("User")
public class User {
    @Id
    @GeneratedValue
    private long ID;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<UserAddress> addresses = new ArrayList<>();

    private boolean isAdmin;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void addAddress(UserAddress userAddress) {
        addresses.add(userAddress);
    }

    public void removeAddress(UserAddress userAddress) {
        addresses.remove(userAddress);
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserAddress> getAddresses() {
        return addresses;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
