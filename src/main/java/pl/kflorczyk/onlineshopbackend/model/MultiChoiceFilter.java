package pl.kflorczyk.onlineshopbackend.model;

import pl.kflorczyk.onlineshopbackend.filter_products.AlternativeParameter;
import pl.kflorczyk.onlineshopbackend.filter_products.FilterParameter;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class MultiChoiceFilter extends Filter {

    public boolean isSuitable(Feature feature, FilterParameter filterValue) {
        AlternativeParameter params = (AlternativeParameter) filterValue;
        return params.contains(feature.getValue());
    }

}
