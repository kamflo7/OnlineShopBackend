package pl.kflorczyk.onlineshopbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.dto.FeatureBagDTO;
import pl.kflorczyk.onlineshopbackend.dto.ProductDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.product_filters.FilterParameters;
import pl.kflorczyk.onlineshopbackend.repositories.CategoryLogicRepository;
import pl.kflorczyk.onlineshopbackend.repositories.ProductRepository;
import pl.kflorczyk.onlineshopbackend.validators.ProductValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private CategoryLogicRepository categoryLogicRepository;

    public ProductService(@Autowired ProductRepository productRepository, @Autowired CategoryLogicRepository categoryLogicRepository) {
        this.productRepository = productRepository;
        this.categoryLogicRepository = categoryLogicRepository;
    }

    public Product getProduct(long productID) {
        return productRepository.findOne(productID);
    }

    public List<Product> getProducts(long categoryID) {
        return getProducts(categoryID, null);
    }

    public List<Product> getProducts(long categoryID, FilterParameters parameters) {
        return getProducts(categoryLogicRepository.findOne(categoryID), parameters);
    }

    public List<Product> getProducts(CategoryLogic categoryLogic) {
        return getProducts(categoryLogic, null);
    }

    public List<Product> getProducts(CategoryLogic categoryLogic, FilterParameters parameters) {
        if(categoryLogic == null) return null;

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

    private boolean isProductSuitable(Product product, Map<FeatureDefinition, List<FeatureValue>> givenFilters) {
        if(givenFilters == null)
            return true;

        for(FeatureBag featureBag : product.getFeatureBags()) {
            for(Map.Entry<FeatureDefinition, List<FeatureValue>> entry : givenFilters.entrySet()) {
                if(entry.getKey().isDummyPriceFilter()) { // todo: dummy solution, don't have enough time, fix later
                    BigDecimal min = new BigDecimal(entry.getValue().get(0).getValue());
                    BigDecimal max = new BigDecimal(entry.getValue().get(1).getValue());

                    if(product.getPrice().compareTo(min) == -1 || product.getPrice().compareTo(max) == 1)
                        return false;

                    continue;
                }

                if(entry.getKey().getId() == featureBag.getFeatureDefinition().getId()) {
                    if(featureBag.getFeatureDefinition().isMultipleValues()) {
                        // Product has FeatureDef("Wireless connectivity") and its List<FeatureValue> has MANY elements
                        // Filter has many elements
                        int matchParams = 0;

                        for(FeatureValue filterValue : entry.getValue()) {
                            for(FeatureValue productFeatureValue : featureBag.getFeatureValues()) {
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
                        FeatureValue featureValue = featureBag.getFeatureValues().get(0);
                        List<FeatureValue> filterValues = entry.getValue();

                        boolean isPresent = filterValues.stream().filter(fv -> fv.getID() == featureValue.getID()).findAny().isPresent();

                        if(!isPresent)
                            return false;
                    }
                }
            }
        }

        return true;
    }

    private Product createProduct(CategoryLogic categoryLogic, String name, String description, BigDecimal price, int amount, List<FeatureBagDTO> features) {
        ProductValidator validator = new ProductValidator(name, description);
        if(!validator.validateName()) {
            throw new InvalidProductNameException("Invalid product name");
        } else if(!validator.validateDescription()) {
            throw new InvalidProductDescriptionException("Invalid product description");
        }

        Product product = new Product(name, price, amount, description, categoryLogic);
        for(FeatureBagDTO featureBagDTO : features) {
            FeatureBag featureBag = new FeatureBag(featureBagDTO.getFeatureDefinition(), featureBagDTO.getFeatureValues());
            product.addFeature(featureBag);
        }
        productRepository.saveAndFlush(product);

        return product;
    }

    public Product createProduct(long categoryLogicID, String name, String description, BigDecimal price, int amount, Map<Long, List<Long>> features) {
        CategoryLogic categoryLogic = categoryLogicRepository.findOne(categoryLogicID);

        if(categoryLogic == null) {
            throw new CategoryNotFoundException("Category not found for given ID");
        }

        List<FeatureBagDTO> outputTranslatedFeatures = convertProductRawDataToFeatureBagDTO(categoryLogic, features);
        return createProduct(categoryLogic, name, description, price, amount, outputTranslatedFeatures);
    }

    public Product createProduct(long categoryLogicID, ProductDTO productDTO) {
        return createProduct(categoryLogicID, productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getAmount(), productDTO.getFeatures());
    }

    public Product editProduct(long productID, ProductDTO data) {
        if(data == null)
            throw new NullPointerException("ProductDTO is null");

        Product product = productRepository.findOne(productID);
        if(product == null)
            throw new ProductNotFoundException("Product not found for given id");

        CategoryLogic categoryLogic = product.getCategoryLogic();
        ProductValidator validator = new ProductValidator(data.getName(), data.getDescription());

        if(data.getName() != null) { // update name if present
            if(!validator.validateName()) {
                throw new InvalidProductNameException("Invalid product name");
            }
            product.setName(data.getName());
        }

        if(data.getDescription() != null) { // update description if present
            if(!validator.validateDescription()) {
                throw new InvalidProductDescriptionException("Invalid product description");
            }
            product.setDescription(data.getDescription());
        }

        if(data.getPrice() != null) // update price if present
            product.setPrice(data.getPrice());

        if(data.getAmount() != null) // update amount if present
            product.setAmount(data.getAmount());

        Map<Long, List<Long>> rawFeatures = data.getFeatures();
        if(rawFeatures != null) { // update feature values if present
            List<FeatureBagDTO> realFeatureValues = convertProductRawDataToFeatureBagDTO(categoryLogic, rawFeatures);
            for(FeatureBagDTO featureBagDTO : realFeatureValues) {
                Optional<FeatureBag> realFeatureBag = product.getFeatureBags().stream().filter(f -> f.getFeatureDefinition().getId() == featureBagDTO.getFeatureDefinition().getId()).findAny();
                if(realFeatureBag.isPresent()) {
                    realFeatureBag.get().setFeatureValues(featureBagDTO.getFeatureValues());
                }
            }
        }

        productRepository.saveAndFlush(product);
        return product;
    }

    public List<FeatureBagDTO> convertProductRawDataToFeatureBagDTO(CategoryLogic categoryLogic, Map<Long, List<Long>> features) {
        List<FeatureBagDTO> outputTranslatedFeatures = new ArrayList<>();
        List<FeatureDefinition> featureDefinitions = categoryLogic.getFeatureDefinitions();
        for(Map.Entry<Long, List<Long>> givenFeature : features.entrySet()) {
            boolean found = false;
            FeatureBagDTO featureBagDTO = null;
            FeatureDefinition featureDefinition = null;

            // #1 - look for appropriate FeatureDefinition by given ID
            for(FeatureDefinition featureDef : featureDefinitions) {
                if(featureDef.getId() == givenFeature.getKey().longValue()) {
                    found = true;
                    featureDefinition = featureDef;
                    featureBagDTO = new FeatureBagDTO(featureDef);
                    break;
                }
            }

            if(!found) {
                throw new IncompatibleFeatureDefinitionAssignmentException("Given FeatureDefinition not found for given CategoryLogic");
            }

            // #2 - look for appropriate FeatureValue's by given IDs for particular FeatureDefinition
            List<FeatureValue> featureValueDefinitions = featureDefinition.getFeatureValueDefinitions();
            for(Long givenFeatureValue : givenFeature.getValue()) {
                boolean foundValue = false;
                for(FeatureValue featureValueDef : featureValueDefinitions) {
                    if(featureValueDef.getID() == givenFeatureValue.longValue()) {
                        foundValue = true;
                        featureBagDTO.addFeatureValue(featureValueDef);
                        break;
                    }
                }

                if(!foundValue) {
                    throw new IncompatibleFeatureValueDefinitionAssignmentException(
                            String.format("Given FeatureValue[%d] not found for given CategoryLogic->FeatureDefinition [%d]", givenFeatureValue, featureDefinition.getId()));
                }
            }

            outputTranslatedFeatures.add(featureBagDTO);
        }
        return outputTranslatedFeatures;
    }
}