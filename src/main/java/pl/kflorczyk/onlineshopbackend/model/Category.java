package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long ID;

    private String name;

    @OneToOne
    private Category parent;

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
