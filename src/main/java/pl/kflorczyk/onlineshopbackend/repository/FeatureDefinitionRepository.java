package pl.kflorczyk.onlineshopbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kflorczyk.onlineshopbackend.model.FeatureDefinition;

public interface FeatureDefinitionRepository extends JpaRepository<FeatureDefinition, Long> {
}
