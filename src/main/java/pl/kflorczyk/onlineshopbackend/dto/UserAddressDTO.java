package pl.kflorczyk.onlineshopbackend.dto;

public abstract class UserAddressDTO {
    private String addressName;
    private String street, zipCode, city, phoneNumber;
    private int houseNumber;

    public UserAddressDTO() {
    }

    public UserAddressDTO(int houseNumber, String zipCode, String city, String phoneNumber) {
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.phoneNumber = phoneNumber;
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
