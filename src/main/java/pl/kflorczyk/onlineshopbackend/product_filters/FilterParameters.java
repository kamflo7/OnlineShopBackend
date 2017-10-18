package pl.kflorczyk.onlineshopbackend.product_filters;

import pl.kflorczyk.onlineshopbackend.model.FeatureDefinition;
import pl.kflorczyk.onlineshopbackend.model.FeatureValue;

import java.util.*;

public class FilterParameters {
    public static final String FILTER_PRICE = "prc";

    private Map<FeatureDefinition, List<FeatureValue>> map = new HashMap<>();

    public FilterParameters() {}

    public FilterParameters(Map<FeatureDefinition, List<FeatureValue>> map) {
        this.map = map;
    }

    public FilterParameters(String urlParams) {
        FilterParametersURLConverter converter = new FilterParametersURLConverter(urlParams);
        this.map = converter.convert();
    }

    //todo: Maybe check if user adds multiple value to FeatureDefinition that consists only of one value?
    public void addFilterValue(FeatureDefinition featureDefinition, FeatureValue featureValue) {
        if(!map.containsKey(featureDefinition)) {
            map.put(featureDefinition, new ArrayList<>());
        }

        map.get(featureDefinition).add(featureValue);
    }

    public void removeFilterValue(FeatureDefinition featureDefinition, FeatureValue featureValue) {
        List<FeatureValue> set = map.get(featureDefinition);
        if(set != null)
            set.remove(featureValue);
    }

    public void removeFilter(FeatureDefinition featureDefinition) {
        map.remove(featureDefinition);
    }

    public Map<FeatureDefinition, List<FeatureValue>> getMap() {
        return Collections.unmodifiableMap(map);
    }

    public int size() {
        return map == null ? 0 : map.size();
    }
}
