package pl.kflorczyk.onlineshopbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.repositoriesAndServices.CategoryLogicRepository;
import pl.kflorczyk.onlineshopbackend.repositoriesAndServices.CategoryViewRepository;
import pl.kflorczyk.onlineshopbackend.validators.CategoryValidator;
import pl.kflorczyk.onlineshopbackend.validators.FeatureDefinitionValidator;
import pl.kflorczyk.onlineshopbackend.validators.FeatureGroupValidator;
import pl.kflorczyk.onlineshopbackend.validators.SimpleNameValidator;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryViewRepository categoryViewRepository;
    private CategoryLogicRepository categoryLogicRepository;

    public CategoryService(@Autowired CategoryViewRepository categoryViewRepository, @Autowired CategoryLogicRepository categoryLogicRepository) {
        this.categoryViewRepository = categoryViewRepository;
        this.categoryLogicRepository = categoryLogicRepository;
    }

    public List<CategoryView> getCategoriesViews() {
        return categoryViewRepository.findAll();
    }

    public List<CategoryLogic> getCategoriesLogic() { return categoryLogicRepository.findAll(); }

    public CategoryLogic findCategoryLogic(String name) {
        return categoryLogicRepository.findByNameIgnoreCase(name);
    }

    public CategoryLogic createNewCategory(String categoryName) {
        if(!new CategoryValidator().validate(categoryName)) {
            throw new InvalidCategoryNameException("Invalid name");
        }

        CategoryLogic byName = categoryLogicRepository.findByNameIgnoreCase(categoryName);
        if(byName != null) {
            return null;
        }

        CategoryLogic categoryLogic = new CategoryLogic(categoryName);
        categoryLogicRepository.saveAndFlush(categoryLogic);
        return categoryLogic;
    }

    public FeatureGroup createFeatureGroup(String name, CategoryLogic categoryLogic) {
        if(!new FeatureGroupValidator().validate(name)) {
            throw new InvalidFeatureGroupNameException("Invalid name");
        }

        FeatureGroup featureGroup = new FeatureGroup(name);
        categoryLogic.addFeatureGroup(featureGroup);
        return featureGroup;
    }

    public FeatureGroup createFeatureGroup(String name, long categoryID) {
        CategoryLogic categoryLogic = categoryLogicRepository.findOne(categoryID);

        if(categoryLogic == null) {
            throw new CategoryNotFoundException("Category not found for given ID");
        }

        return createFeatureGroup(name, categoryLogic);
    }

    public FeatureDefinition createFeatureDefinition(String name, boolean multipleValues, boolean filterable, boolean visible, List<FeatureValue> values, FeatureGroup featureGroup, CategoryLogic categoryLogic) {
        if(!new FeatureDefinitionValidator().validate(name)) {
            throw new InvalidFeatureGroupNameException("Invalid name");
        }

        SimpleNameValidator validator = new SimpleNameValidator(3);
        values.stream().forEach( f -> {
            if(!validator.validate(f.getValue())) {
                throw new InvalidFeatureValueDefinitionException("Invalid value for FeatureValue");
            }
        });

        FeatureDefinition featureDefinition = new FeatureDefinition(name, featureGroup, filterable, multipleValues, visible);
        categoryLogic.addFeatureDefinition(featureDefinition);
        featureDefinition.setFeatureValueDefinitions(values);
        return featureDefinition;
    }

    public FeatureDefinition createFeatureDefinition(String name, boolean multipleValues, boolean filterable, boolean visible, List<FeatureValue> values, long featureGroupID, long categoryID) {
        CategoryLogic categoryLogic = categoryLogicRepository.findOne(categoryID);

        if(categoryLogic == null) {
            throw new CategoryNotFoundException("Category not found for given ID");
        }

        Optional<FeatureGroup> featureGroup = categoryLogic.getFeatureGroups().stream().filter(f -> f.getId() == featureGroupID).findAny();

        if(!featureGroup.isPresent()) {
            throw new FeatureGroupNotFoundException("FeatureGroup not found for given ID");
        }

        return createFeatureDefinition(name, multipleValues, filterable, visible, values, featureGroup.get(), categoryLogic);
    }


}
