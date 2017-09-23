package pl.kflorczyk.onlineshopbackend.validators;

public class PasswordValidator {

    public boolean validate(String password) {
        return password != null && password.length() >= 6;
    }
}
