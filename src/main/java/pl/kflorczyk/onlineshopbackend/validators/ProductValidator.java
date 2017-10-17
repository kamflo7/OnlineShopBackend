package pl.kflorczyk.onlineshopbackend.validators;

public class ProductValidator {

    private String name, description;

    public ProductValidator(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public boolean validateName() {
        if(name == null || name.length() < 3)
            return false;
        return true;
    }

    public boolean validateDescription() {
        return true;
    }
}
