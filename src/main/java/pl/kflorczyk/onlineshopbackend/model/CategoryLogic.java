package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.*;

@Entity
public class CategoryLogic {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long ID;

    private String name;

//    @OneToOne
//    @JoinColumn(name = "parent_id")
//    private CategoryLogic parent;


    public CategoryLogic() {}

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

//    public CategoryLogic getParent() {
//        return parent;
//    }
}
