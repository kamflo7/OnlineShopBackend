package pl.kflorczyk.onlineshopbackend.model;

import pl.kflorczyk.onlineshopbackend.dto.PersonAddressDTO;

import javax.persistence.Entity;

@Entity
public class PersonAddress extends UserAddress {
    private String firstName, lastName;

    public PersonAddress() {
    }

    public PersonAddress(boolean isDefault) {
        super(isDefault);
    }

    public PersonAddress(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PersonAddress(boolean isDefault, String firstName, String lastName) {
        super(isDefault);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PersonAddress(PersonAddressDTO dto) {
        super(dto);
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
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
