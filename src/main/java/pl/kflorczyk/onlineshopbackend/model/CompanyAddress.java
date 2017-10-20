package pl.kflorczyk.onlineshopbackend.model;

import pl.kflorczyk.onlineshopbackend.dto.CompanyAddressDTO;

import javax.persistence.Entity;

@Entity
public class CompanyAddress extends UserAddress {
    private String name, name2, name3, nip;

    public CompanyAddress() {
    }

    public CompanyAddress(boolean isDefault) {
        super(isDefault);
    }

    public CompanyAddress(String name, String name2, String name3, String nip) {
        this.name = name;
        this.name2 = name2;
        this.name3 = name3;
        this.nip = nip;
    }

    public CompanyAddress(boolean isDefault, String name, String name2, String name3, String nip) {
        super(isDefault);
        this.name = name;
        this.name2 = name2;
        this.name3 = name3;
        this.nip = nip;
    }

    public CompanyAddress(CompanyAddressDTO dto) {
        super(dto);
        this.name = dto.getName();
        this.name2 = dto.getName2();
        this.name3 = dto.getName3();
        this.nip = dto.getNip();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }
}
