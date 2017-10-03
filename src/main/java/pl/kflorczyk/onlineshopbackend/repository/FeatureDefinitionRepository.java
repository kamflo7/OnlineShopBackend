package pl.kflorczyk.onlineshopbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.FeatureDefinition;

import java.util.List;

public interface FeatureDefinitionRepository extends JpaRepository<FeatureDefinition, Long> {
    List<FeatureDefinition> findByCategoryLogic(CategoryLogic categoryLogic);
}
