package pl.kflorczyk.onlineshopbackend.validators;

import java.util.Optional;

public class PasswordValidator {

    public boolean validate(String password) {
        return Optional.ofNullable(password)
                .filter(c -> c.length() >= 6)
                .isPresent();
    }
}
