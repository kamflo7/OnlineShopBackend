package pl.kflorczyk.onlineshopbackend.exceptions;

public class UserAddressNotFoundException extends RuntimeException {
    public UserAddressNotFoundException(String s) {
        super(s);
    }
}
