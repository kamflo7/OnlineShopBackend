package pl.kflorczyk.onlineshopbackend.validators;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public boolean validate(String email) {
        return Optional.ofNullable(email)
                    .map(e -> VALID_EMAIL_ADDRESS_REGEX.matcher(e).find())
                    .orElse(false);
    }
}
