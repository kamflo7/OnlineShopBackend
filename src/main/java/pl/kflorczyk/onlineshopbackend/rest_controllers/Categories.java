package pl.kflorczyk.onlineshopbackend.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.FeatureGroup;
import pl.kflorczyk.onlineshopbackend.rest_controllers.responses.Response;
import pl.kflorczyk.onlineshopbackend.services.CategoryService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Categories {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(path = "/categories/{id}")
    public CategoryLogic getCategory(@PathVariable long id) {
        CategoryLogic categoryLogic = categoryService.getCategoryLogic(id);
        return categoryLogic;
    }

//    @PutMapping(path = "/categories")
//    public CategoryResponse createCategory(@RequestParam(name = "name") String name, HttpServletRequest request) {
//        CategoryLogic categoryLogic = null;
//
//        try {
//            categoryLogic = categoryService.createNewCategory(name);
//        } catch(InvalidCategoryNameException e) {
//            return new CategoryResponse(AbstractResponse.STATUS_FAILURE, e.getMessage());
//        } catch(CategoryAlreadyExistsException e) {
//            return new CategoryResponse(AbstractResponse.STATUS_FAILURE, e.getMessage());
//        }
//
//        return new CategoryResponse(CategoryResponse.STATUS_SUCCESS, categoryLogic);
//    }

    @PutMapping(path = "/categories")
    public Response<CategoryLogic> createCategory(@RequestParam(name = "name") String name, HttpServletRequest request) {
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
}
