package pl.kflorczyk.onlineshopbackend.validators;

import java.util.Optional;

public class ProductValidator {

    public boolean validateName(String name) {
        return Optional.ofNullable(name)
                .filter(c -> c.length() >= 3)
                .isPresent();
    }

    public boolean validateDescription(String description) {
        return Optional.ofNullable(description)
                .filter(c -> c.length() >= 6)
                .isPresent();
    }
}
