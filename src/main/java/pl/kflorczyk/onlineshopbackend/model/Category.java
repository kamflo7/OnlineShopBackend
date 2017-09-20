package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long ID;

    private String name;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

//    @OneToMany(mappedBy = "category")
//    private List<Product> products;

    public Category() {}

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }
}
