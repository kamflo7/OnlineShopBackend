package pl.kflorczyk.onlineshopbackend.validators;

import java.util.Optional;

public class CategoryValidator {

    public CategoryValidator() {
    }

    public boolean validate(String categoryName) {
        return Optional.ofNullable(categoryName)
                .filter(c -> c.length() >= 3)
                .isPresent();
    }
}
