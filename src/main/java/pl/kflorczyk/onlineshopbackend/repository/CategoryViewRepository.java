package pl.kflorczyk.onlineshopbackend.repository;

import org.springframework.data.repository.CrudRepository;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.CategoryView;

import java.util.List;

public interface CategoryViewRepository extends CrudRepository<CategoryView, Long> {
    List<CategoryView> findAll();
}
