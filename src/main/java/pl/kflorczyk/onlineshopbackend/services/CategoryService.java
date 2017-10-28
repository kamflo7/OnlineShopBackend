package pl.kflorczyk.onlineshopbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTO;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTOEditable;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.repositories.CategoryLogicRepository;
import pl.kflorczyk.onlineshopbackend.repositories.CategoryViewRepository;
import pl.kflorczyk.onlineshopbackend.validators.CategoryValidator;
import pl.kflorczyk.onlineshopbackend.validators.FeatureDefinitionValidator;
import pl.kflorczyk.onlineshopbackend.validators.FeatureGroupValidator;
import pl.kflorczyk.onlineshopbackend.validators.SimpleNameValidator;

import java.util.*;

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

    public CategoryLogic getCategoryLogic(String name) {
        return categoryLogicRepository.findByNameIgnoreCase(name);
    }

    public CategoryLogic getCategoryLogic(long ID) {
        return categoryLogicRepository.findOne(ID);
    }

    public CategoryLogic createNewCategory(String categoryName) {
        if(!new CategoryValidator().validate(categoryName)) {
            throw new InvalidCategoryNameException("Invalid name");
        }

        CategoryLogic byName = categoryLogicRepository.findByNameIgnoreCase(categoryName);
        if(byName != null) {
            throw new CategoryAlreadyExistsException("The name for category is already taken");
        }

        CategoryLogic categoryLogic = new CategoryLogic(categoryName);
        categoryLogicRepository.saveAndFlush(categoryLogic);
        return categoryLogic;
    }

    public CategoryLogic createFeatureGroup(String name, CategoryLogic categoryLogic) {
        if(!new FeatureGroupValidator().validate(name)) {
            throw new InvalidFeatureGroupNameException("Invalid name");
        }

        Optional<FeatureGroup> foundTheSameGroupName = categoryLogic.getFeatureGroups().stream().filter(f -> f.getName().equals(name)).findAny();
        if(foundTheSameGroupName.isPresent()) {
            throw new InvalidFeatureGroupNameException("This name is already taken for existing group in this category");
        }

        FeatureGroup featureGroup = new FeatureGroup(name);
        categoryLogic.addFeatureGroup(featureGroup);
        categoryLogicRepository.saveAndFlush(categoryLogic);
        return categoryLogic;
    }

    public CategoryLogic createFeatureGroup(String name, long categoryID) {
        CategoryLogic categoryLogic = categoryLogicRepository.findOne(categoryID);

        if(categoryLogic == null) {
            throw new CategoryNotFoundException("Category not found for given ID");
        }

        return createFeatureGroup(name, categoryLogic);
    }

    public CategoryLogic createFeatureDefinition(FeatureDefinitionDTO featureDefinitionDTO, FeatureGroup featureGroup, CategoryLogic categoryLogic) {
        if(!new FeatureDefinitionValidator().validate(featureDefinitionDTO.getName())) {
            throw new InvalidFeatureDefinitionNameException("Invalid name");
        }

        if(categoryLogic.getFeatureDefinitions().stream().filter(f -> f.getName().equals(featureDefinitionDTO.getName())).findAny().isPresent()) {
            throw new FeatureDefinitionAlreadyExists("The given name for FeatureDefinition is already taken");
        }

        SimpleNameValidator validator = new SimpleNameValidator(3);
        featureDefinitionDTO.getNewValues().stream().forEach(f -> {
            if(!validator.validate(f)) {
                throw new InvalidFeatureValueDefinitionException("Invalid value for FeatureValue");
            }
        });

        FeatureDefinition featureDefinition = new FeatureDefinition(featureDefinitionDTO.getName(), featureGroup, featureDefinitionDTO.isFilterable(), featureDefinitionDTO.isMultipleValues(), featureDefinitionDTO.isVisible());
        categoryLogic.addFeatureDefinition(featureDefinition);

        List<FeatureValue> featureValues = new ArrayList<>(featureDefinitionDTO.getNewValues().size());
        featureDefinitionDTO.getNewValues().stream().forEach(f -> featureValues.add(new FeatureValue(f)));

        featureDefinition.setFeatureValueDefinitions(featureValues);
        categoryLogicRepository.saveAndFlush(categoryLogic);
        return categoryLogic;
    }

    public CategoryLogic createFeatureDefinition(FeatureDefinitionDTO featureDefinitionDTO, long featureGroupID, long categoryID) {
        CategoryLogic categoryLogic = categoryLogicRepository.findOne(categoryID);

        if(categoryLogic == null) {
            throw new CategoryNotFoundException("Category not found for given ID");
        }

        Optional<FeatureGroup> featureGroup = categoryLogic.getFeatureGroups().stream().filter(f -> f.getId() == featureGroupID).findAny();

        if(!featureGroup.isPresent()) {
            throw new FeatureGroupNotFoundException("FeatureGroup not found for given ID");
        }

        return createFeatureDefinition(featureDefinitionDTO, featureGroup.get(), categoryLogic);
    }

    public CategoryLogic editCategoryLogic(long ID, String newName) {
        if(!new CategoryValidator().validate(newName)) {
            throw new InvalidCategoryNameException("Invalid name");
        }

        CategoryLogic categoryLogic = categoryLogicRepository.findOne(ID);
        if(categoryLogic == null) {
            throw new CategoryNotFoundException("The name for category is already taken");
        }

        categoryLogic.setName(newName);
        categoryLogicRepository.saveAndFlush(categoryLogic);
        return categoryLogic;
    }

    public CategoryLogic editFeatureGroup(long categoryID, long featureGroupID, String newName) {
        if(!new FeatureGroupValidator().validate(newName)) {
            throw new InvalidFeatureGroupNameException("Invalid name");
        }

        CategoryLogic categoryLogic = categoryLogicRepository.findOne(categoryID);
        if(categoryLogic == null) {
            throw new CategoryNotFoundException("The name for category is already taken");
        }

        Optional<FeatureGroup> featureGroup = categoryLogic.getFeatureGroups().stream().filter(f -> f.getId() == featureGroupID).findAny();
        if(!featureGroup.isPresent()) {
            throw new FeatureGroupNotFoundException("FeatureGroup not found for given id");
        }

        featureGroup.get().setName(newName);
        categoryLogicRepository.saveAndFlush(categoryLogic);
        return categoryLogic;
    }

    public CategoryLogic editFeatureDefinition(long categoryID, long featureGroupID, long featureDefinitionID, FeatureDefinitionDTOEditable newFeatureDefinition) {
        if(!new FeatureDefinitionValidator().validate(newFeatureDefinition.getName())) {
            throw new InvalidFeatureDefinitionNameException("Invalid name");
        }

        Map<Long, String> givenNewValues = newFeatureDefinition.getValues();

        SimpleNameValidator validator = new SimpleNameValidator(3);
        if(givenNewValues != null) {
            givenNewValues.forEach((k, v) -> {
                if (!validator.validate(v)) {
                    throw new InvalidFeatureValueDefinitionException("Invalid value for FeatureValue");
                }
            });
        }

        List<String> additionalNewValues = newFeatureDefinition.getNewValues();
        if(additionalNewValues != null) {
            additionalNewValues.forEach(v -> {
                if (!validator.validate(v)) {
                    throw new InvalidFeatureValueDefinitionException("Invalid value for FeatureValue");
                }
            });
        }

        CategoryLogic categoryLogic = categoryLogicRepository.findOne(categoryID);
        if(categoryLogic == null) {
            throw new CategoryNotFoundException("Category not found for given ID");
        }

        if(categoryLogic.getFeatureDefinitions().stream().filter(f ->  f.getName().equals(newFeatureDefinition.getName()) && f.getId() != featureDefinitionID).findAny().isPresent()) {
            throw new FeatureDefinitionAlreadyExists("The given name for FeatureDefinition is already taken");
        }

        Optional<FeatureDefinition> featureDefinition = categoryLogic.getFeatureDefinitions().stream().filter(f -> f.getId() == featureDefinitionID).findAny();
        if(!featureDefinition.isPresent()) {
            throw new FeatureDefinitionNotFoundException("FeatureDefinition not found for given ID");
        }

        Optional<FeatureGroup> featureGroup = categoryLogic.getFeatureGroups().stream().filter(f -> f.getId() == featureGroupID).findAny();
        if(!featureGroup.isPresent()) {
            throw new FeatureGroupNotFoundException("FeatureGroup not found for given id");
        }

        FeatureDefinition oldFeatureDefinition = featureDefinition.get();
        if(oldFeatureDefinition.isMultipleValues() && !newFeatureDefinition.isMultipleValues() && !newFeatureDefinition.isForceUpdate()) {
            throw new FeatureDefinitionCriticalOperationNotAuthorizedException("Trying to change multipleValues from true to false without 'forceUpdate' flag");
        }

        oldFeatureDefinition.setMultipleValues(newFeatureDefinition.isMultipleValues());
        oldFeatureDefinition.setFilterable(newFeatureDefinition.isFilterable());
        oldFeatureDefinition.setVisible(newFeatureDefinition.isVisible());
        oldFeatureDefinition.setName(newFeatureDefinition.getName());
        oldFeatureDefinition.setFeatureGroup(featureGroup.get());
        if(givenNewValues != null) {
            for (Map.Entry<Long, String> givenEntry : newFeatureDefinition.getValues().entrySet()) {
                for (FeatureValue featureValue : oldFeatureDefinition.getFeatureValueDefinitions()) {
                    if (givenEntry.getKey() == featureValue.getID()) {
                        featureValue.setValue(givenEntry.getValue());
                    }
                }
            }
        }

        if(additionalNewValues != null) {
            for(String newValue : additionalNewValues) {
                oldFeatureDefinition.getFeatureValueDefinitions().add(new FeatureValue(newValue));
            }
        }

        categoryLogicRepository.saveAndFlush(categoryLogic);
        return categoryLogic;
    }

    public CategoryView getCategoryView(long id) {
        return categoryViewRepository.findOne(id);
    }

    public List<CategoryView> getCategoriesView() {
        return categoryViewRepository.findAll();
    }

    public CategoryView createCategoryView(String name, Long parentID, Long categoryLogicID, Map<Long, List<Long>> features) {
        SimpleNameValidator validator = new SimpleNameValidator(2);
        if(name == null || !validator.validate(name)) {
            throw new InvalidCategoryNameException("Invalid name for category");
        }

        CategoryLogic categoryLogic = null;
        if(categoryLogicID != null) {
            categoryLogic = categoryLogicRepository.findOne(categoryLogicID);

            if(categoryLogic == null) {
                throw new CategoryNotFoundException("CategoryLogic not found for given ID");
            }
        }

        CategoryView parent = null;
        if(parentID != null) {
            parent = categoryViewRepository.findOne(parentID);
            if(parent == null) {
                throw new CategoryViewNotFoundException("CategoryView (parent) not found for given ID");
            }
        }

        if(features != null && categoryLogicID == null) {
            throw new NullPointerException("If features are present, the categoryLogicID should also be present");
        }

        Map<FeatureDefinition, FeatureValueGroup> translatedMap = null;
        if(features != null) {
            try {
                translatedMap = translateNavigationFeaturesIndexesToEntities(features, categoryLogic);
            } catch (IncompatibleFeatureDefinitionAssignmentException | IncompatibleFeatureValueDefinitionAssignmentException e) {
                throw e;
            }
        }

        CategoryView categoryView = new CategoryView(name);
        categoryView.setParent(parent);
        categoryView.setCategoryLogic(categoryLogic);
        categoryView.setFilters(translatedMap);

        categoryViewRepository.saveAndFlush(categoryView);
        return categoryView;
    }

    private Map<FeatureDefinition, FeatureValueGroup> translateNavigationFeaturesIndexesToEntities(Map<Long, List<Long>> rawMap, CategoryLogic categoryLogic) {
        Map<FeatureDefinition, FeatureValueGroup> result = new HashMap<>();
        for(Map.Entry<Long, List<Long>> rawDefinitionIndex : rawMap.entrySet()) {
            Optional<FeatureDefinition> appropriateDef = categoryLogic.getFeatureDefinitions().stream().filter(f -> f.getId() == rawDefinitionIndex.getKey()).findAny();
            if(!appropriateDef.isPresent()) {
                throw new IncompatibleFeatureDefinitionAssignmentException("Incompatible filters in terms of CategoryLogic");
            }

            List<FeatureValue> featureValues = new ArrayList<>();
            for(Long rawFeatureValue : rawDefinitionIndex.getValue()) {
                Optional<FeatureValue> appropriateValueDef = appropriateDef.get().getFeatureValueDefinitions().stream().filter(fv -> fv.getID() == rawFeatureValue).findAny();
                if(!appropriateValueDef.isPresent()) {
                    throw new IncompatibleFeatureValueDefinitionAssignmentException("Incompatible filters in terms of CategoryLogic");
                }

                featureValues.add(appropriateValueDef.get());
            }
            result.put(appropriateDef.get(), new FeatureValueGroup(featureValues));
        }
        return result;
    }
}
