package pl.kflorczyk.onlineshopbackend.exceptions;

public class CategoryViewNotFoundException extends RuntimeException {
    public CategoryViewNotFoundException(String s) {
        super(s);
    }
}
