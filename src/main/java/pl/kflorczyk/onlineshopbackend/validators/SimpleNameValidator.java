package pl.kflorczyk.onlineshopbackend.validators;

import java.util.Optional;

public class SimpleNameValidator {

    public boolean validate(String name, int minLength) {
        return Optional.ofNullable(name)
                .filter(c -> c.length() >= minLength)
                .isPresent();
    }

    public boolean validate(String name, int minLength, int maxLength) {
        return Optional.ofNullable(name)
                .filter(c -> c.length() >= minLength && c.length() <= maxLength)
                .isPresent();
    }

}
