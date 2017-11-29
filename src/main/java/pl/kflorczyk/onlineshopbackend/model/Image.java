package pl.kflorczyk.onlineshopbackend.model;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@JsonFilter("Image")
public class Image {

    @Id
    @GeneratedValue
    private long id;

    @Lob
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public Image() { }
}
