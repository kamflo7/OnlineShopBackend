package pl.kflorczyk.onlineshopbackend.repositoriesAndServices;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kflorczyk.onlineshopbackend.model.CategoryView;

import java.util.List;

public interface CategoryViewRepository extends JpaRepository<CategoryView, Long> {
    List<CategoryView> findAll();
}
