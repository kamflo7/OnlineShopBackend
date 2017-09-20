package pl.kflorczyk.onlineshopbackend.repository;

import org.springframework.data.repository.CrudRepository;
import pl.kflorczyk.onlineshopbackend.model.Category;

import java.util.List;

public interface CategoriesRepository extends CrudRepository<Category, Long> {
    List<Category> findAll();
}
