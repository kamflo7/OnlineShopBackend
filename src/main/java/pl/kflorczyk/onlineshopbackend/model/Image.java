package pl.kflorczyk.onlineshopbackend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Image {
    private final String PREFIX = "img";

    @Id
    @GeneratedValue
    private long id;

    public String getName() {
        return String.format("%s%d.jpg", PREFIX, id);
    }

    public Image() { }
}
