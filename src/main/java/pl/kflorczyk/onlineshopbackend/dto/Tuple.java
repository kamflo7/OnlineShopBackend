package pl.kflorczyk.onlineshopbackend.dto;

public class Tuple<T> {
    public T item1;
    public T item2;

    public Tuple(T item1, T item2) {
        this.item1 = item1;
        this.item2 = item2;
    }
}
