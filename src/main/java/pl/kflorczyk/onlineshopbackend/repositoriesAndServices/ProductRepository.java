package pl.kflorczyk.onlineshopbackend.repositoriesAndServices;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();

    Product findByName(String name);

    List<Product> findByCategoryLogic(CategoryLogic categoryLogic);

//    @Modifying
//    @Query(value = "INSERT INTO category_logic (name) VALUES (:name)", nativeQuery = true)
//    @Transactional
//    void saveCategoryLogic(@Param("name") String name);
//
//    @Query(value = "SELECT id, name FROM category_logic WHERE name = (:name) LIMIT 1", nativeQuery = true)
//    @Transactional
//    CategoryLogic findCategoryLogic(@Param("name") String name);
}