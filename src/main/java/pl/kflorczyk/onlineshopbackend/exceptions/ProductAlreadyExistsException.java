package pl.kflorczyk.onlineshopbackend.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String s) {
        super(s);
    }
}
