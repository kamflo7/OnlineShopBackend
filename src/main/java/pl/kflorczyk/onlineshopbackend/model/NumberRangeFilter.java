package pl.kflorczyk.onlineshopbackend.model;

import pl.kflorczyk.onlineshopbackend.filter_products.FilterParameter;
import pl.kflorczyk.onlineshopbackend.filter_products.RangeParameter;

import javax.persistence.Entity;

@Entity
public class NumberRangeFilter extends Filter {
    private String expression;

    public NumberRangeFilter(String expression) {
        this.expression = expression;
    }

    public boolean isSuitable(Feature feature, FilterParameter filterValue) {
        RangeParameter params = (RangeParameter) filterValue;
        float value = Float.parseFloat(feature.getValue());

        return value >= params.min && value <= params.max;
    }
}
