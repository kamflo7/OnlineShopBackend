package pl.kflorczyk.onlineshopbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.product_filters.FilterParameters;
import pl.kflorczyk.onlineshopbackend.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(@Autowired ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts(CategoryLogic categoryLogic) {
        return getProducts(categoryLogic, null);
    }

    public List<Product> getProducts(CategoryLogic categoryLogic, FilterParameters parameters) {
        List<Product> received = productRepository.findByCategoryLogic(categoryLogic);
        List<Product> result;

        if(parameters != null && parameters.size() != 0) {
            result = new ArrayList<>();
            Map<FeatureDefinition, List<FeatureValue>> map = parameters.getMap();

            for(Product p : received) {
                if(isProductSuitable(p, map)) {
                    result.add(p);
                }
            }
        } else {
            result = received;
        }

        return result;
    }

    private boolean isProductSuitable(Product product, Map<FeatureDefinition, List<FeatureValue>> map) {
        for(Feature feature : product.getFeatures()) {
            for(Map.Entry<FeatureDefinition, List<FeatureValue>> entry : map.entrySet()) {
                if(entry.getKey().getId() == feature.getFeatureDefinition().getId()) {
                    if(feature.getFeatureDefinition().isMultipleValues()) {
                        // Product has FeatureDef("Wireless connectivity") and its List<FeatureValue> has MANY elements
                        // Filter has many elements
                        int matchParams = 0;

                        for(FeatureValue filterValue : entry.getValue()) {
                            for(FeatureValue productFeatureValue : feature.getFeatureValues()) {
                                if(filterValue.getID() == productFeatureValue.getID())
                                    matchParams++;
                            }
                        }

                        if(matchParams != entry.getValue().size()) {
                            return false;
                        }
                    } else {
                        // Product has FeatureDef("RAM") and its List<FeatureValue> with one element
                        // Filter has many elements
                        FeatureValue featureValue = feature.getFeatureValues().get(0);
                        List<FeatureValue> filterValues = entry.getValue();

                        boolean isPresent = filterValues.stream().filter(fv -> fv.getID() == featureValue.getID()).findAny().isPresent();

                        if(!isPresent)
                            return false;
//                        if(!entry.getValue().contains(featureValue)) {
//                            return false;
//                        }
                    }
                }
            }
        }

        return true;
    }

}