package pl.kflorczyk.onlineshopbackend.repository;

import org.springframework.data.repository.CrudRepository;
import pl.kflorczyk.onlineshopbackend.model.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();
}