package pl.kflorczyk.onlineshopbackend.model;

import pl.kflorczyk.onlineshopbackend.dto.UserAddressDTO;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserAddress {
    @Id
    @GeneratedValue
    private long ID;

    private boolean isDefault;

    private String addressName;
    private String street, zipCode, city, phoneNumber;
    private int houseNumber;

    public UserAddress() {}

    public UserAddress(UserAddressDTO dto) {
        this.addressName = dto.getAddressName();
        this.street = dto.getStreet();
        this.houseNumber = dto.getHouseNumber();
        this.zipCode = dto.getZipCode();
        this.city = dto.getCity();
        this.phoneNumber = dto.getPhoneNumber();
    }

    public UserAddress(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
