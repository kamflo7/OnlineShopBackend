package pl.kflorczyk.onlineshopbackend.dto;

public class CompanyAddressDTO extends UserAddressDTO {
    private String name, name2, name3, nip;

    public CompanyAddressDTO() {
    }

    public CompanyAddressDTO(int houseNumber, String zipCode, String city, String phoneNumber, String name, String nip) {
        super(houseNumber, zipCode, city, phoneNumber);
        this.name = name;
        this.name2 = name2;
        this.name3 = name3;
        this.nip = nip;
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
