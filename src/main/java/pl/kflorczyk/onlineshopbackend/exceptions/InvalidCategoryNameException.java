package pl.kflorczyk.onlineshopbackend.exceptions;

public class InvalidCategoryNameException extends RuntimeException {
    public InvalidCategoryNameException(String msg) {
        super(msg);
    }
}
