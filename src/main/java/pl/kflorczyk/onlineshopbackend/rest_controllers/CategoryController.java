package pl.kflorczyk.onlineshopbackend.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTO;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTOEditable;
import pl.kflorczyk.onlineshopbackend.dto.ProductDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.Product;
import pl.kflorczyk.onlineshopbackend.product_filters.FilterParameters;
import pl.kflorczyk.onlineshopbackend.rest_controllers.responses.Response;
import pl.kflorczyk.onlineshopbackend.rest_controllers.responses.ResponseDetail;
import pl.kflorczyk.onlineshopbackend.services.CategoryService;
import pl.kflorczyk.onlineshopbackend.services.ProductService;

import java.util.*;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    // Category section
    @GetMapping(path = "/categories")
    public String getCategories() {
        List<CategoryLogic> categories = categoryService.getCategoriesLogic();

        String result = null;
        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(Claimant.MANY_CATEGORIES_LOGIC))
                    .writeValueAsString(new ResponseDetail<>(categories));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    @GetMapping(path = "/categories/{id}")
    public String getCategory(@PathVariable long id) {
        CategoryLogic categoryLogic = categoryService.getCategoryLogic(id);

        if(categoryLogic == null) {
            try {
                return new ObjectMapper().writeValueAsString(new ResponseDetail<CategoryLogic>(ResponseDetail.Status.FAILURE, "CategoryLogic not found for given ID"));
            } catch (JsonProcessingException e) {
                return null;
            }
        }

        String result = null;
        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(Claimant.CATEGORY_LOGIC))
                    .writeValueAsString(new ResponseDetail<>(categoryLogic));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    @PutMapping(path = "/categories")
    public String createCategory(@RequestParam(name = "name") String name) {
        CategoryLogic categoryLogic = null;

        try {
            categoryLogic = categoryService.createNewCategory(name);
        } catch(InvalidCategoryNameException | CategoryAlreadyExistsException e) {
            try {
                return new ObjectMapper()
                        .writer(getJSONFilters(Claimant.CATEGORY_LOGIC))
                        .writeValueAsString(new ResponseDetail<>(e.getMessage()));
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
                return null;
            }
        }

        String result = null;
        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(Claimant.CATEGORY_LOGIC))
                    .writeValueAsString(new ResponseDetail<>(categoryLogic));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    @PostMapping(path = "/categories/{id}")
    public String editCategory(@PathVariable long id, @RequestParam(name = "name") String name) {
        CategoryLogic edited = null;
        try {
            edited = categoryService.editCategoryLogic(id, name);
        } catch(InvalidCategoryNameException | CategoryNotFoundException e) {
            return mapToJSON(Claimant.CATEGORY_LOGIC, new ResponseDetail<>(e.getMessage()));
        }
        return mapToJSON(Claimant.CATEGORY_LOGIC, new ResponseDetail<>(edited));
    }

    // FeatureGroup section
    @PutMapping(path = "/categories/{categoryID}/feature_groups")
    public ResponseDetail<CategoryLogic> createFeatureGroup(
            @PathVariable(name = "categoryID") long categoryID,
            @RequestParam(name = "name") String name) {
        CategoryLogic categoryLogic = null;

        try {
            categoryLogic = categoryService.createFeatureGroup(name, categoryID);
        } catch(CategoryNotFoundException | InvalidFeatureGroupNameException e) {
            return new ResponseDetail<>(ResponseDetail.Status.FAILURE, e.getMessage());
        }

        return new ResponseDetail<>(categoryLogic);
    }

    @PostMapping(path = "/categories/{categoryID}/feature_groups/{groupID}")
    public String editFeatureGroup(
            @PathVariable(name = "categoryID") long categoryID,
            @PathVariable(name = "groupID") long groupID,
            @RequestParam(name = "name") String name
    ) {
        CategoryLogic edited = null;
        try {
            edited = categoryService.editFeatureGroup(categoryID, groupID, name);
        } catch(InvalidFeatureGroupNameException | CategoryNotFoundException | FeatureGroupNotFoundException e) {
            return mapToJSON(Claimant.CATEGORY_LOGIC, new ResponseDetail<>(e.getMessage()));
        }
        return mapToJSON(Claimant.CATEGORY_LOGIC, new ResponseDetail<>(edited));
    }
    // Product section
    @PutMapping(path = "/categories/{categoryID}/products")
    public String createProduct(
        @PathVariable(name = "categoryID") long categoryID,
        @RequestBody ProductDTO productDTO
    ) { // not the shortest method because returned type has been changed from ResponseType<T> to String
        // in case of sudden neccessary use @JsonFilters
        Product product = null;

        try {
            product = productService.createProduct(categoryID, productDTO);
        } catch(IncompatibleFeatureValueDefinitionAssignmentException |
                IncompatibleFeatureDefinitionAssignmentException |
                CategoryNotFoundException |
                InvalidProductNameException |
                InvalidProductDescriptionException |
                ProductAlreadyExistsException e) {
            try {
                return new ObjectMapper().writeValueAsString(new ResponseDetail<>(ResponseDetail.Status.FAILURE, e.getMessage()));
            } catch (JsonProcessingException e1) {
                return null;
            }
        }

        String result = null;
        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(Claimant.ONE_PRODUCT))
                    .writeValueAsString(new ResponseDetail<>(product));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping(path = "/products/test/{productID}")
    public String editProductTEST(
            @PathVariable(name = "productID") long productID,
            @RequestBody ProductDTO productDTO
    ) {
        return null;
    }

    @PostMapping(path = "/products/{productID}")
    public String editProduct(
            @PathVariable(name = "productID") long productID,
            @RequestBody ProductDTO productDTO
    ) {
        Product product = null;

        try {
            product = productService.editProduct(productID, productDTO);
        } catch(IncompatibleFeatureValueDefinitionAssignmentException |
                IncompatibleFeatureDefinitionAssignmentException |
                CategoryNotFoundException |
                InvalidProductNameException |
                InvalidProductDescriptionException |
                ProductAlreadyExistsException e) {
            try {
                return new ObjectMapper().writeValueAsString(new ResponseDetail<>(ResponseDetail.Status.FAILURE, e.getMessage()));
            } catch (JsonProcessingException e1) {
                return null;
            }
        }

        String result = null;
        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(Claimant.ONE_PRODUCT))
                    .writeValueAsString(new ResponseDetail<>(product));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping(path = "/categories/{categoryID}/products")
    public String getProducts(
            @PathVariable(name = "categoryID") long categoryID,
            @RequestParam(name = "f", required = false) String filters
    ) {
        FilterParameters filterParameters = new FilterParameters(filters == null ? "" : filters);
        List<Product> products = productService.getProducts(categoryID, filterParameters);

        String result = null;
        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(Claimant.MANY_PRODUCTS))
                    .writeValueAsString(new ResponseDetail<>(products));
        } catch (JsonProcessingException e) {
            return null;
        }

        return result;
    }

    @GetMapping(path = "/products/{productID}")
    public String getProduct(@PathVariable(name = "productID") long productID) {
        Product product = productService.getProduct(productID);

        String result = null;
        try {
            ResponseDetail responseDetail = product == null ? new ResponseDetail<>(Response.Status.FAILURE, "Product not found")
                                                : new ResponseDetail<>(product);

            result = new ObjectMapper()
                    .writer(getJSONFilters(Claimant.ONE_PRODUCT))
                    .writeValueAsString(responseDetail);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    // FeatureDefinition section
    @PutMapping(path = "/categories/{categoryID}/feature_groups/{groupID}/feature_definitions")
    public String createFeatureDefinition(
            @PathVariable(name = "categoryID") long categoryID,
            @PathVariable(name = "groupID") long groupID,
            @RequestBody FeatureDefinitionDTO featureDTO
    ) {
        CategoryLogic categoryLogic = null;
        String result = null;

        try {
            categoryLogic = categoryService.createFeatureDefinition(featureDTO, groupID, categoryID);
        } catch(CategoryNotFoundException | FeatureDefinitionAlreadyExists | FeatureGroupNotFoundException | InvalidFeatureDefinitionNameException | InvalidFeatureValueDefinitionException e) {
            try {
                return new ObjectMapper().writeValueAsString(new ResponseDetail<CategoryLogic>(ResponseDetail.Status.FAILURE, e.getMessage()));
            } catch (JsonProcessingException e1) {
                return null;
            }
        }

        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(Claimant.CATEGORY_LOGIC))
                    .writeValueAsString(new ResponseDetail<>(categoryLogic));
        } catch (JsonProcessingException e) {
            return null;
        }
        return result;
    }

    @PostMapping(path = "/categories/{categoryID}/feature_groups/{groupID}/feature_definitions/{featureID}")
    public String editFeatureDefinition(
            @PathVariable(name = "categoryID") long categoryID,
            @PathVariable(name = "groupID") long groupID,
            @PathVariable(name = "featureID") long featureID,
            @RequestBody FeatureDefinitionDTOEditable featureDefinitionDTOEditable
            ) {
        CategoryLogic categoryLogicRefreshed = null;
        String result = null;

        try {
            categoryLogicRefreshed = categoryService.editFeatureDefinition(categoryID, groupID, featureID, featureDefinitionDTOEditable);
        } catch(InvalidFeatureDefinitionNameException |
                InvalidFeatureValueDefinitionException |
                CategoryNotFoundException |
                FeatureDefinitionNotFoundException |
                FeatureGroupNotFoundException |
                FeatureDefinitionCriticalOperationNotAuthorizedException e) {
            try {
                return new ObjectMapper().writeValueAsString(new ResponseDetail<CategoryLogic>(ResponseDetail.Status.FAILURE, e.getMessage()));
            } catch (JsonProcessingException e1) {
                return null;
            }
        }

        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(Claimant.CATEGORY_LOGIC))
                    .writeValueAsString(new ResponseDetail<>(categoryLogicRefreshed));
        } catch (JsonProcessingException e) {
            return null;
        }

        return result;
    }

    private FilterProvider getJSONFilters(Claimant claimant) {
        if(claimant == Claimant.MANY_PRODUCTS) {
            return new SimpleFilterProvider()
                    .addFilter("Product", SimpleBeanPropertyFilter.serializeAllExcept("categoryLogic"))
                    .addFilter("FeatureDefinition", SimpleBeanPropertyFilter.serializeAllExcept(
                                    "categoryLogic", "featureGroup", "featureValueDefinitions",
                                    "visible", "filterable", "multipleValues", "name"));
        } else if(claimant == Claimant.ONE_PRODUCT) {
//            return new SimpleFilterProvider()
//                    .addFilter("Product", SimpleBeanPropertyFilter.serializeAllExcept("categoryLogic"))
//                    .addFilter("FeatureBag", SimpleBeanPropertyFilter.serializeAllExcept("id"))
//                    .addFilter("FeatureValue", SimpleBeanPropertyFilter.serializeAllExcept("id"))
//                    .addFilter("FeatureDefinition", SimpleBeanPropertyFilter.serializeAllExcept(
//                            "id", "featureGroup", "categoryLogic", "featureValueDefinitions"
//                    ));
            return new SimpleFilterProvider()
                    .addFilter("Product", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("CategoryLogic", SimpleBeanPropertyFilter.filterOutAllExcept("id"))
                    .addFilter("FeatureBag", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("FeatureValue", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("FeatureDefinition", SimpleBeanPropertyFilter.serializeAllExcept(
                            "featureGroup", "categoryLogic", "featureValueDefinitions"
                    ));
        } else if(claimant == Claimant.CATEGORY_LOGIC) {
            return new SimpleFilterProvider()
                    .addFilter("Product", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("FeatureValue", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("FeatureDefinition", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("CategoryLogic", SimpleBeanPropertyFilter.serializeAll());
        } else if(claimant == Claimant.MANY_CATEGORIES_LOGIC) {
            return new SimpleFilterProvider()
                    .addFilter("Product", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("FeatureValue", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("FeatureDefinition", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("CategoryLogic", SimpleBeanPropertyFilter.serializeAllExcept(
                            "featureDefinitions", "featureGroups"
                    ));
        }
        return null;
    }

    private enum Claimant {
        ONE_PRODUCT,
        MANY_PRODUCTS,
        CATEGORY_LOGIC,
        MANY_CATEGORIES_LOGIC
    }

    private String mapToJSON(Claimant claimant, Object valueToMap) {
        String result = null;
        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(claimant))
                    .writeValueAsString(valueToMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{\"status\": \"failure\", \"description\": \"internal error\"}";
        }
        return result;
    }
}
