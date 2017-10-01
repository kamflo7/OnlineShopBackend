package pl.kflorczyk.onlineshopbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kflorczyk.onlineshopbackend.model.FeatureGroup;

public interface FeatureGroupRepository extends JpaRepository<FeatureGroup, Long> {

}
