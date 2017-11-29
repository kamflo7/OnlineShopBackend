package pl.kflorczyk.onlineshopbackend.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.dto.FeatureBagDTO;
import pl.kflorczyk.onlineshopbackend.dto.ProductDTO;
import pl.kflorczyk.onlineshopbackend.dto.Tuple;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.product_filters.FilterParameters;
import pl.kflorczyk.onlineshopbackend.product_filters.ProductSortTranslator;
import pl.kflorczyk.onlineshopbackend.repositories.CategoryLogicRepository;
import pl.kflorczyk.onlineshopbackend.repositories.ImageRepository;
import pl.kflorczyk.onlineshopbackend.repositories.ProductRepository;
import pl.kflorczyk.onlineshopbackend.validators.ProductValidator;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    @NonNull private final ProductRepository productRepository;
    @NonNull private final CategoryLogicRepository categoryLogicRepository;

    @NonNull private final ProductValidator productValidator;

    public Tuple<BigDecimal> getPriceRange(long categoryID) {
        CategoryLogic categoryLogic = categoryLogicRepository.findOne(categoryID);

        if(categoryLogic == null)
            return null;

        Product lowestPrice = productRepository.findFirst1ByCategoryLogicOrderByPriceAsc(categoryLogic);
        Product highestPrice = productRepository.findFirst1ByCategoryLogicOrderByPriceDesc(categoryLogic);
        return new Tuple<>(lowestPrice.getPrice(), highestPrice.getPrice());
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Product getProduct(long productID) {
        return productRepository.findOne(productID);
    }

    public List<Product> getProducts(long categoryID) {
        return getProducts(categoryID, null, null);
    }

    public List<Product> getProducts(long categoryID, FilterParameters parameters, String sort) {
        return getProducts(categoryLogicRepository.findOne(categoryID), parameters, sort);
    }

    public List<Product> getProducts(CategoryLogic categoryLogic) {
        return getProducts(categoryLogic, null, null);
    }

    public List<Product> getProducts(CategoryLogic categoryLogic, FilterParameters parameters, String sortString) {
        if(categoryLogic == null) return null;

        Sort sort = new ProductSortTranslator(sortString).translate();

        List<Product> received = productRepository.findByCategoryLogic(categoryLogic, sort);
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
                if(entry.getKey().isDummyPriceFilter()) {
                    BigDecimal min = new BigDecimal(entry.getValue().get(0).getValue());
                    BigDecimal max = new BigDecimal(entry.getValue().get(1).getValue());

                    if(product.getPrice().compareTo(min) == -1 || product.getPrice().compareTo(max) == 1)
                        return false;

                    continue;
                }

                if(entry.getKey().getId() == featureBag.getFeatureDefinition().getId()) {
                    if(featureBag.getFeatureDefinition().isMultipleValues()) {
                        int matchParams = 0;

                        for(FeatureValue filterValue : entry.getValue()) {
                            for(FeatureValue productFeatureValue : featureBag.getFeatureValues()) {
                                if(filterValue.getID() == productFeatureValue.getID())
                                    matchParams++;
                            }
                        }

                        if(matchParams == 0) {
                            return false;
                        }
                    } else {
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

    @NonNull private final ImageRepository imageRepository;

    private Product createProduct(CategoryLogic categoryLogic, String name, String description, BigDecimal price, int amount, List<FeatureBagDTO> features, String image) {
        if(!productValidator.validateName(name)) {
            throw new InvalidProductNameException("Invalid product name");
        } else if(!productValidator.validateDescription(description)) {
            throw new InvalidProductDescriptionException("Invalid product description");
        }

        Product alreadyExists = productRepository.findFirst1ByNameIgnoreCase(name);
        if(alreadyExists != null) {
            throw new ProductAlreadyExistsException("The given name is already taken");
        }

        Product product = new Product(name, price, amount, description, categoryLogic);
        for(FeatureBagDTO featureBagDTO : features) {
            FeatureBag featureBag = new FeatureBag(featureBagDTO.getFeatureDefinition(), featureBagDTO.getFeatureValues());
            product.addFeature(featureBag);
        }

        Image img = null;
        if(image != null) {
            byte[] decode = Base64.getDecoder().decode(image);
            img = new Image();
            img.setData(decode);

            imageRepository.saveAndFlush(img);
        }

        product.setImage(img);
        productRepository.saveAndFlush(product);

        return product;
    }

    public Product createProduct(long categoryLogicID, String name, String description, BigDecimal price, int amount, Map<Long, List<Long>> features, String image) {
        CategoryLogic categoryLogic = categoryLogicRepository.findOne(categoryLogicID);

        if(categoryLogic == null) {
            throw new CategoryNotFoundException("Category not found for given ID");
        }

        List<FeatureBagDTO> outputTranslatedFeatures = convertProductRawDataToFeatureBagDTO(categoryLogic, features);
        return createProduct(categoryLogic, name, description, price, amount, outputTranslatedFeatures, image);
    }

    public Product createProduct(long categoryLogicID, ProductDTO productDTO) {
        return createProduct(categoryLogicID, productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getAmount(), productDTO.getFeatures(), productDTO.getImage());
    }

    public Product editProduct(long productID, ProductDTO data) {
        if(data == null)
            throw new NullPointerException("ProductDTO is null");

        Product product = productRepository.findOne(productID);
        if(product == null)
            throw new ProductNotFoundException("Product not found for given id");

        CategoryLogic categoryLogic = product.getCategoryLogic();

        if(data.getName() != null) {
            if(!productValidator.validateName(data.getName())) {
                throw new InvalidProductNameException("Invalid product name");
            }

            Product alreadyExists = productRepository.findFirst1ByNameIgnoreCase(data.getName());
            if(alreadyExists != null && productID != alreadyExists.getID()) {
                throw new ProductAlreadyExistsException("The given name is already taken");
            }
            product.setName(data.getName());
        }

        if(data.getDescription() != null) {
            if(!productValidator.validateDescription(data.getDescription())) {
                throw new InvalidProductDescriptionException("Invalid product description");
            }
            product.setDescription(data.getDescription());
        }

        if(data.getPrice() != null)
            product.setPrice(data.getPrice());

        if(data.getAmount() != null)
            product.setAmount(data.getAmount());

        Map<Long, List<Long>> rawFeatures = data.getFeatures();
        if(rawFeatures != null) {
            List<FeatureBagDTO> realFeatureValues = convertProductRawDataToFeatureBagDTO(categoryLogic, rawFeatures);
            for(FeatureBagDTO featureBagDTO : realFeatureValues) {
                Optional<FeatureBag> realFeatureBag = product.getFeatureBags().stream().filter(f -> f.getFeatureDefinition().getId() == featureBagDTO.getFeatureDefinition().getId()).findAny();
                if(realFeatureBag.isPresent()) {
                    realFeatureBag.get().setFeatureValues(featureBagDTO.getFeatureValues());
                } else {
                    product.addFeature(new FeatureBag(featureBagDTO.getFeatureDefinition(), featureBagDTO.getFeatureValues()));
                }
            }
        }

        Image newlyCreatedImage = null;
        if(data.getImage() != null) {
            if(product.getImage() != null) {
                Image image = imageRepository.findOne(product.getImage().getId());
                byte[] decode = Base64.getDecoder().decode(data.getImage());
                image.setData(decode);
                imageRepository.saveAndFlush(image);
            } else {
                Image image = new Image();
                byte[] decode = Base64.getDecoder().decode(data.getImage());
                image.setData(decode);
                imageRepository.saveAndFlush(image);
                newlyCreatedImage = image;
            }
        }

        if(newlyCreatedImage != null) {
            product.setImage(newlyCreatedImage);
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