package pl.kflorczyk.onlineshopbackend.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import pl.kflorczyk.onlineshopbackend.dto.*;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.product_filters.FilterParameters;
import pl.kflorczyk.onlineshopbackend.repositories.CategoryLogicRepository;
import pl.kflorczyk.onlineshopbackend.repositories.ProductRepository;
import pl.kflorczyk.onlineshopbackend.repositories.UserRepository;
import pl.kflorczyk.onlineshopbackend.rest_controllers.responses.Response;
import pl.kflorczyk.onlineshopbackend.rest_controllers.responses.ResponseDetail;
import pl.kflorczyk.onlineshopbackend.services.CategoryService;
import pl.kflorczyk.onlineshopbackend.services.ProductService;
import pl.kflorczyk.onlineshopbackend.services.UserService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor // WYEPIEPRZYC
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
    public String editCategory(@PathVariable(name = "id") long id, @RequestParam(name = "name") String name) {
        CategoryLogic edited = null;
        try {
            edited = categoryService.editCategoryLogic(id, name);
        } catch(InvalidCategoryNameException | CategoryNotFoundException e) {
            return mapToJSON(Claimant.CATEGORY_LOGIC, new ResponseDetail<>(e.getMessage()));
        }
        return mapToJSON(Claimant.CATEGORY_LOGIC, new ResponseDetail<>(edited));
    }

    @GetMapping("/navigations/{id}")
    public String getNavigation(@PathVariable(name = "id") long id) {
        CategoryView categoryView = categoryService.getCategoryView(id);
        return mapToJSON(Claimant.CATEGORY_VIEW, new ResponseDetail<>(categoryView));
    }

    @GetMapping("/navigations/{id}/tree")
    public String getNavigationTree(@PathVariable(name= "id") long id) {
        CategoryView categoryView = categoryService.getCategoryView(id);
        return mapToJSON(Claimant.CATEGORY_VIEW, new ResponseDetail<>(categoryView));
    }

    @GetMapping("/navigations")
    public String getNavigations() {
        List<CategoryView> categoriesView = categoryService.getCategoriesView();
        return mapToJSON(Claimant.CATEGORY_VIEW, new ResponseDetail<>(categoriesView));
    }

    @PutMapping("/navigations")
    public String createNavigation(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "parentID", required = false) Long parentID,
            @RequestParam(name = "categoryLogicID", required = false) Long categoryLogicID
    ) {
        CategoryView categoryView = null;
        try {
            categoryView = categoryService.createCategoryView(name, parentID, categoryLogicID);
        } catch(InvalidCategoryNameException |
                CategoryNotFoundException |
                CategoryViewNotFoundException |
                NullPointerException |
                IncompatibleFeatureDefinitionAssignmentException |
                IncompatibleFeatureValueDefinitionAssignmentException e) {
            return mapToJSON(Claimant.CATEGORY_VIEW, new Response(Response.Status.FAILURE, e.getMessage()));
        }
        return mapToJSON(Claimant.CATEGORY_VIEW, new ResponseDetail<>(categoryView));
    }

    @PostMapping("/navigations/{id}")
    public String updateNavigation(
        @PathVariable(name = "id") long navigationID,
        @RequestParam(name = "name") String name,
        @RequestParam(name = "parentID", required = false) Long parentID,
        @RequestParam(name = "categoryLogicID", required = false) Long categoryLogicID
    ) {
        CategoryView categoryView = null;
        try {
            categoryView = categoryService.editCategoryView(navigationID, name, parentID, categoryLogicID);
        } catch(InvalidCategoryNameException | CategoryViewNotFoundException
                | CategoryNotFoundException e) {
            return mapToJSON(Claimant.CATEGORY_VIEW, new ResponseDetail<>(Response.Status.FAILURE, e.getMessage()));
        }
        return mapToJSON(Claimant.CATEGORY_VIEW, new ResponseDetail<>(categoryView));
    }

    @DeleteMapping("/navigations/{id}")
    public String removeNavigation(@PathVariable(name = "id") long navigationID) {
        int childrenAffected = categoryService.removeNavigation(navigationID);
        return mapToJSON(Claimant.EMPTY, new Response(Response.Status.SUCCESS, "Children affected: " + childrenAffected));
    }

// FeatureGroup section
    @PutMapping(path = "/categories/{categoryID}/feature_groups")
    public String createFeatureGroup(
            @PathVariable(name = "categoryID") long categoryID,
            @RequestParam(name = "name") String name) {
        CategoryLogic categoryLogic = null;

        try {
            categoryLogic = categoryService.createFeatureGroup(name, categoryID);
        } catch(CategoryNotFoundException | InvalidFeatureGroupNameException e) {
            return mapToJSON(Claimant.CATEGORY_LOGIC, new ResponseDetail<>(e.getMessage()));
        }

        return mapToJSON(Claimant.CATEGORY_LOGIC, new ResponseDetail<>(categoryLogic));
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
    ) {
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
            @RequestParam(name = "f", required = false) String filters,
            @RequestParam(name = "s", required = false) String sort
    ) {
        FilterParameters filterParameters = new FilterParameters(filters == null ? "" : filters);
        List<Product> products = productService.getProducts(categoryID, filterParameters, sort);

        String result = null;
        try {
            result = new ObjectMapper()
                    .writer(getJSONFilters(Claimant.MANY_PRODUCTS))
                    .writeValueAsString(new ResponseDetail<>(products));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    @GetMapping(path = "/categories/{categoryID}/products/price-range")
    public String getProductsPriceRange(
            @PathVariable(name = "categoryID") long categoryID
    ) {
        Tuple<BigDecimal> priceRange = productService.getPriceRange(categoryID);
        if(priceRange == null) {
            return mapToJSON(Claimant.EMPTY, new Response(Response.Status.FAILURE, "null"));
        }

        return mapToJSON(Claimant.EMPTY, new ResponseDetail<>(priceRange));
    }

    @GetMapping(path = "/products/search/{name}")
    public String searchProductsByName(@PathVariable(name = "name") String name) {
        List<Product> products = productService.searchByName(name);
        return mapToJSON(Claimant.MANY_PRODUCTS, new ResponseDetail<>(products));
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
                                    "visible", "filterable", "multipleValues", "name"))
                    .addFilter("FeatureBag", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("FeatureDefinition", SimpleBeanPropertyFilter.serializeAllExcept("featureGroup", "categoryLogic", "featureValueDefinitions"))
                    .addFilter("FeatureValue", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("Image", SimpleBeanPropertyFilter.filterOutAllExcept("id"));
        } else if(claimant == Claimant.ONE_PRODUCT) {
            return new SimpleFilterProvider()
                    .addFilter("Product", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("CategoryLogic", SimpleBeanPropertyFilter.filterOutAllExcept("id"))
                    .addFilter("FeatureBag", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("FeatureValue", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("FeatureDefinition", SimpleBeanPropertyFilter.serializeAllExcept(
                            "featureGroup", "categoryLogic", "featureValueDefinitions"
                    ))
                    .addFilter("Image", SimpleBeanPropertyFilter.filterOutAllExcept("id"));
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
        } else if(claimant == Claimant.CATEGORY_VIEW) {
            return new SimpleFilterProvider()
                    .addFilter("CategoryView", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("CategoryLogic", SimpleBeanPropertyFilter.serializeAllExcept("featureDefinitions", "featureGroups"))
                    .addFilter("FeatureDefinition", SimpleBeanPropertyFilter.serializeAllExcept(
                            "featureGroup", "categoryLogic", "featureValueDefinitions", "multipleValues", "filterable", "visible"))
                    .addFilter("FeatureValue", SimpleBeanPropertyFilter.serializeAll());
        } else if(claimant == Claimant.EMPTY) {
            return new SimpleFilterProvider();
        }
        return null;
    }

    private enum Claimant {
        EMPTY,
        ONE_PRODUCT,
        MANY_PRODUCTS,

        CATEGORY_LOGIC,
        MANY_CATEGORIES_LOGIC,

        CATEGORY_VIEW,
    }

    private String mapToJSON(Claimant claimant, Object valueToMap) {
        String result = null;
        try {
            if(claimant == null)
                result = new ObjectMapper().writeValueAsString(valueToMap);
            else
                result = new ObjectMapper().writer(getJSONFilters(claimant)).writeValueAsString(valueToMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{\"status\": \"failure\", \"description\": \"internal error\"}";
        }
        return result;
    }
}
