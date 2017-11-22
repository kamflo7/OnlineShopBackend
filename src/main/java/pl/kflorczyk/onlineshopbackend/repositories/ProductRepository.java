package pl.kflorczyk.onlineshopbackend.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();

    Product findFirst1ByNameIgnoreCase(String name);

    List<Product> findByCategoryLogic(CategoryLogic categoryLogic, Sort sort);

    List<Product> findByNameContainingIgnoreCase(String name);

    Product findFirst1ByCategoryLogicOrderByPriceDesc(CategoryLogic categoryLogic);
    Product findFirst1ByCategoryLogicOrderByPriceAsc(CategoryLogic categoryLogic);
}