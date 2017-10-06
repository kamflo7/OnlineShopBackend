package pl.kflorczyk.onlineshopbackend.model;

import pl.kflorczyk.onlineshopbackend.filter_products.FilterParameter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Filter {
    @Id
    @GeneratedValue
    private long id;

    public abstract boolean isSuitable(Feature feature, FilterParameter filterValue);
}