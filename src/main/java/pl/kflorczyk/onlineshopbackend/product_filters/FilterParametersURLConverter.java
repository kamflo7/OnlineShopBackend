package pl.kflorczyk.onlineshopbackend.product_filters;

import pl.kflorczyk.onlineshopbackend.model.FeatureDefinition;
import pl.kflorczyk.onlineshopbackend.model.FeatureValue;

import java.math.BigDecimal;
import java.util.*;

public class FilterParametersURLConverter {
    private String url;

    public FilterParametersURLConverter(String urlFilters) {
        this.url = urlFilters;
    }

    public Map<FeatureDefinition, List<FeatureValue>> convert() {
        Map<FeatureDefinition, List<FeatureValue>> map = new HashMap<>();

        String[] filters = url.split(",");
        if(filters.length == 1 && filters[0].isEmpty())
            return null;

        for(String filter : filters) {
            String[] parts = filter.split("-");
            if(parts.length != 2) continue;

            if(parts[0].equals(FilterParameters.FILTER_PRICE)) { // todo: dummy solution, don't have enough time, fix later
                String[] values = parts[1].split("_");
                if(values.length != 2) continue;

                try {
                    new BigDecimal(values[0]);
                    new BigDecimal(values[1]);
                } catch(NumberFormatException e) {
                    continue;
                }

                List<FeatureValue> featureValues = new ArrayList<>(2);
                featureValues.add(new FeatureValue(values[0]));
                featureValues.add(new FeatureValue(values[1]));

                map.put(FeatureDefinition.forPriceFilter(), featureValues);
            } else {
                String[] values = parts[1].split("\\.");

                long id;
                try {
                    id = Long.parseLong(parts[0]);
                } catch(NumberFormatException e) {
                    continue;
                }

                FeatureDefinition featureDefinition = FeatureDefinition.ofID(id);
                if (!map.containsKey(featureDefinition)) {
                    map.put(featureDefinition, new ArrayList<>());
                }

                for (String value : values) {
                    long valueID;
                    try {
                        valueID = Long.parseLong(value);
                    } catch(NumberFormatException e) {
                        continue;
                    }

                    map.get(featureDefinition).add(FeatureValue.ofID(valueID));
                }
            }
        }

        return map;
    }
}
