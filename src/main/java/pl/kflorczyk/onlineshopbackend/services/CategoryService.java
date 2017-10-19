package pl.kflorczyk.onlineshopbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTO;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTOEditable;
import pl.kflorczyk.onlineshopbackend.dto.ProductDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.repositories.CategoryLogicRepository;
import pl.kflorczyk.onlineshopbackend.repositories.CategoryViewRepository;
import pl.kflorczyk.onlineshopbackend.validators.CategoryValidator;
import pl.kflorczyk.onlineshopbackend.validators.FeatureDefinitionValidator;
import pl.kflorczyk.onlineshopbackend.validators.FeatureGroupValidator;
import pl.kflorczyk.onlineshopbackend.validators.SimpleNameValidator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        SimpleNameValidator validator = new SimpleNameValidator(3);
        featureDefinitionDTO.getValues().stream().forEach( f -> {
            if(!validator.validate(f)) {
                throw new InvalidFeatureValueDefinitionException("Invalid value for FeatureValue");
            }
        });

        FeatureDefinition featureDefinition = new FeatureDefinition(featureDefinitionDTO.getName(), featureGroup, featureDefinitionDTO.isFilterable(), featureDefinitionDTO.isMultipleValues(), featureDefinitionDTO.isVisible());
        categoryLogic.addFeatureDefinition(featureDefinition);

        List<FeatureValue> featureValues = new ArrayList<>(featureDefinitionDTO.getValues().size());
        featureDefinitionDTO.getValues().stream().forEach(f -> featureValues.add(new FeatureValue(f)));
//        featureDefinitionDTO.getValues().forEach(f -> featureValues.add(new FeatureValue(f)));
        // todo: deal with this above

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

    public void editCategoryLogic(long ID, String newName) {
        if(!new CategoryValidator().validate(newName)) {
            throw new InvalidCategoryNameException("Invalid name");
        }

        CategoryLogic categoryLogic = categoryLogicRepository.findOne(ID);
        if(categoryLogic == null) {
            throw new CategoryNotFoundException("The name for category is already taken");
        }

        categoryLogic.setName(newName);
        categoryLogicRepository.saveAndFlush(categoryLogic);
    }

    public void editFeatureGroup(long categoryID, long featureGroupID, String newName) {
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

        CategoryLogic categoryLogic = categoryLogicRepository.findOne(categoryID);
        if(categoryLogic == null) {
            throw new CategoryNotFoundException("Category not found for given ID");
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

        categoryLogicRepository.saveAndFlush(categoryLogic);
        return categoryLogic;
    }
}
