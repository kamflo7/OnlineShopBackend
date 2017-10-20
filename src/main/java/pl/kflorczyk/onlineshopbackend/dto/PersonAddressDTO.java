package pl.kflorczyk.onlineshopbackend.dto;

public class PersonAddressDTO extends UserAddressDTO {
    private String firstName, lastName;

    public PersonAddressDTO() {
    }

    public PersonAddressDTO(int houseNumber, String zipCode, String city, String phoneNumber, String firstName, String lastName) {
        super(houseNumber, zipCode, city, phoneNumber);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

