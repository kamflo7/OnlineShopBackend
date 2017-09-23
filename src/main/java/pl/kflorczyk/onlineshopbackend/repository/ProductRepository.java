package pl.kflorczyk.onlineshopbackend.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.kflorczyk.onlineshopbackend.model.Product;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();

    @Modifying
    @Query(value = "INSERT INTO category_logic (name) VALUES (:name)", nativeQuery = true)
    @Transactional
    void insertCategoryLogic(@Param("name") String name);
}