package pl.kflorczyk.onlineshopbackend.repositoriesAndServices;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;

public interface CategoryLogicRepository extends JpaRepository<CategoryLogic, Long> {
    CategoryLogic findByNameIgnoreCase(String name);
}
