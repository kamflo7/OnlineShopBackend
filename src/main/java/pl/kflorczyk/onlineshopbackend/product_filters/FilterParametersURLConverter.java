package pl.kflorczyk.onlineshopbackend.product_filters;

import pl.kflorczyk.onlineshopbackend.model.FeatureDefinition;
import pl.kflorczyk.onlineshopbackend.model.FeatureValue;

import java.util.*;

public class FilterParametersURLConverter {

    private String url;

    public FilterParametersURLConverter(String urlFilters) {
        this.url = urlFilters;
    }

    public Map<FeatureDefinition, List<FeatureValue>> convert() {
        Map<FeatureDefinition, List<FeatureValue>> map = new HashMap<>();

        String[] filters = url.split(",");
        for(String filter : filters) {
            String[] parts = filter.split("=");
            String[] values = parts[1].split("\\.");

            FeatureDefinition featureDefinition = FeatureDefinition.ofID(Long.parseLong(parts[0]));
            if(!map.containsKey(featureDefinition)) {
                map.put(featureDefinition, new ArrayList<>());
            }

            for(String value : values) {
                map.get(featureDefinition).add(FeatureValue.ofID(Long.parseLong(value)));
            }
        }

        return map;
    }
}
