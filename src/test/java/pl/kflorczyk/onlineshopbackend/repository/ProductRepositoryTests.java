package pl.kflorczyk.onlineshopbackend.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.FeatureGroup;
import pl.kflorczyk.onlineshopbackend.model.Product;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryLogicRepository categoryLogicRepository;

    @Autowired
    private FeatureGroupRepository featureGroupRepository;

    @Test
    public void relationTest() {
        CategoryLogic categoryLogic = new CategoryLogic("Smartphones");

        FeatureGroup featureGroup = new FeatureGroup();
        featureGroup.setCategoryLogic(categoryLogic);
        featureGroup.setName("Podstawowe informacje");

        Product product = new Product("Xiaomi Redmi Note 4", new BigDecimal("980"), 100, "Najnowszy model od Xiaomi");
        product.setCategoryLogic(categoryLogic);


        // ####

        categoryLogicRepository.save(categoryLogic);
        categoryLogicRepository.flush();

        featureGroupRepository.save(featureGroup);
        featureGroupRepository.flush();

        productRepository.save(product);
        productRepository.flush();


        Product obtain = productRepository.findByName("Xiaomi Redmi Note 4");

        assertThat(obtain.getName()).isEqualTo(product.getName());
        assertThat(obtain.getCategoryLogic().getName()).isEqualTo(categoryLogic.getName());

    }

    @Test
    public void some() {
        CategoryLogic categoryLogic = new CategoryLogic("Smartphones");

        Product product = new Product("Xiaomi Redmi Note 4", new BigDecimal("980"), 100, "Najnowszy model od Xiaomi");
        product.setCategoryLogic(categoryLogic);

        Product product2 = new Product("Huawei P9 Lite", new BigDecimal("1980"), 150, "AAAAA model od Xiaomi");
        product2.setCategoryLogic(categoryLogic);

        categoryLogicRepository.save(categoryLogic);
        categoryLogicRepository.flush();

        productRepository.save(product);
        productRepository.save(product2);
        productRepository.flush();

        List<Product> products = productRepository.findAll();

        assertThat(products.size()).isEqualTo(2);
    }
}
