package pl.kflorczyk.onlineshopbackend.validators;

public class CategoryValidator {

    public boolean validate(String categoryName) {
        if(categoryName != null && categoryName.length() > 3) {
            return true;
        }
        return false;
    }
}
