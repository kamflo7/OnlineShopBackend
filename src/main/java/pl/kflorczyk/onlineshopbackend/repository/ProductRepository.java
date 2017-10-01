package pl.kflorczyk.onlineshopbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.Product;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();

    Product findByName(String name);

//    @Modifying
//    @Query(value = "INSERT INTO category_logic (name) VALUES (:name)", nativeQuery = true)
//    @Transactional
//    void saveCategoryLogic(@Param("name") String name);
//
//    @Query(value = "SELECT id, name FROM category_logic WHERE name = (:name) LIMIT 1", nativeQuery = true)
//    @Transactional
//    CategoryLogic findCategoryLogic(@Param("name") String name);
}