package pl.kflorczyk.onlineshopbackend.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTO;
import pl.kflorczyk.onlineshopbackend.dto.ProductDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.FeatureGroup;
import pl.kflorczyk.onlineshopbackend.model.Product;
import pl.kflorczyk.onlineshopbackend.product_filters.FilterParameters;
import pl.kflorczyk.onlineshopbackend.rest_controllers.responses.Response;
import pl.kflorczyk.onlineshopbackend.services.CategoryService;
import pl.kflorczyk.onlineshopbackend.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping(path = "/categories/{id}")
    public Response<CategoryLogic> getCategory(@PathVariable long id) {
        CategoryLogic categoryLogic = categoryService.getCategoryLogic(id);
        return new Response<>(categoryLogic);
    }

    @PutMapping(path = "/categories")
    public Response<CategoryLogic> createCategory(@RequestParam(name = "name") String name) {
        CategoryLogic categoryLogic = null;

        try {
            categoryLogic = categoryService.createNewCategory(name);
        } catch(InvalidCategoryNameException | CategoryAlreadyExistsException e) {
            return new Response<>(Response.STATUS_FAILURE, e.getMessage());
        }

        return new Response<>(categoryLogic);
    }


    @PutMapping(path = "/categories/{categoryID}/feature_groups")
    public Response<CategoryLogic> createFeatureGroup(
            @PathVariable(name = "categoryID") long categoryID,
            @RequestParam(name = "name") String name) {
        CategoryLogic categoryLogic = null;

        try {
            categoryLogic = categoryService.createFeatureGroup(name, categoryID);
        } catch(CategoryNotFoundException | InvalidFeatureGroupNameException e) {
            return new Response<>(Response.STATUS_FAILURE, e.getMessage());
        }

        return new Response<>(categoryLogic);
    }

    @PutMapping(path = "/categories/{categoryID}/feature_groups/{groupID}/features")
    public Response<CategoryLogic> createFeatureDefinition(
            @PathVariable(name = "categoryID") long categoryID,
            @PathVariable(name = "groupID") long groupID,
            @RequestBody FeatureDefinitionDTO featureDTO
    ) {
        CategoryLogic categoryLogic = null;

        try {
            categoryLogic = categoryService.createFeatureDefinition(featureDTO, groupID, categoryID);
        } catch(CategoryNotFoundException | FeatureGroupNotFoundException | InvalidFeatureGroupNameException | InvalidFeatureValueDefinitionException e) {
            return new Response<>(Response.STATUS_FAILURE, e.getMessage());
        }

        return new Response<>(categoryLogic);
    }

    @GetMapping(path = "/categories/{categoryID}/products")
    public Response<List<Product>> getProducts(
            @PathVariable(name = "categoryID") long categoryID,
            @RequestParam(name = "f") String filters
    ) {
        FilterParameters filterParameters = new FilterParameters(filters);
        List<Product> products = productService.getProducts(categoryID, filterParameters);
        return new Response<>(products);
    }

    @PutMapping(path = "/categories/{categoryID}/products")
    public Response<Product> createProduct(
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
                InvalidProductDescriptionException e) {
            return new Response<>(Response.STATUS_FAILURE, e.getMessage());
        }

        return new Response<>(product);
    }
}
