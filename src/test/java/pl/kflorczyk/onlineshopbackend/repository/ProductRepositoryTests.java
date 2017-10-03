package pl.kflorczyk.onlineshopbackend.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.model.*;

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

    @Autowired
    private FeatureDefinitionRepository featureDefinitionRepository;

    @Test
    public void relationTest() {
        // prepare structure - CategoryLogic, its FeatureDefinitions, FeatureGroups
        CategoryLogic categoryLogicSmartphones = new CategoryLogic("Smartfony");
        FeatureGroup featureGroup = new FeatureGroup("Informacje techniczne");

        FeatureDefinition featureDefCpu = new FeatureDefinition("Procesor", featureGroup);
        FeatureDefinition featureDefRAM = new FeatureDefinition("Pamięć RAM", featureGroup);
        FeatureDefinition featureDefInternalStorage = new FeatureDefinition("Pamięć wewnętrzna", featureGroup);

        categoryLogicSmartphones.addFeatureGroup(featureGroup);

        categoryLogicSmartphones.addFeatureDefinition(featureDefCpu);
        categoryLogicSmartphones.addFeatureDefinition(featureDefRAM);
        categoryLogicSmartphones.addFeatureDefinition(featureDefInternalStorage);

        categoryLogicRepository.saveAndFlush(categoryLogicSmartphones);

        // make a Product
        CategoryLogic obtainCategory = categoryLogicRepository.findByName("Smartfony");
        List<FeatureDefinition> obtainFeatureDefinitions = obtainCategory.getFeatureDefinitions();
        List<FeatureGroup> obtainFeatureGroups = obtainCategory.getFeatureGroups();

        assertThat(obtainCategory.getName()).isEqualTo(categoryLogicSmartphones.getName());
        assertThat(obtainFeatureDefinitions).containsSequence(featureDefCpu, featureDefRAM, featureDefInternalStorage);
        assertThat(obtainFeatureGroups).contains(featureGroup);

        Product product = new Product("Xiaomi Redmi Note 4", new BigDecimal("980"), 100, "Najnowszy model od Xiaomi");
        product.setCategoryLogic(categoryLogicSmartphones);

        Feature featureCPU = new Feature(featureDefCpu, "Snapdragon 635");
        Feature featureRAM = new Feature(featureDefRAM, "4GB");
        Feature featureInternalStorage = new Feature(featureDefInternalStorage, "32GB");

        product.addFeature(featureCPU);
        product.addFeature(featureRAM);
        product.addFeature(featureInternalStorage);
        productRepository.save(product);
        // end of making a Product

        Product obtainProduct = productRepository.findByName("Xiaomi Redmi Note 4");
        List<Feature> obtainProductFeatures = obtainProduct.getFeatures();

        assertThat(obtainProduct.getName()).isEqualTo(product.getName());
        assertThat(obtainProductFeatures).containsSequence(featureCPU, featureRAM, featureInternalStorage);

        for(Feature f : obtainProductFeatures) {
            assertThat(f.getFeatureDefinition().getCategoryLogic()).isEqualTo(obtainCategory);
            assertThat(f.getFeatureDefinition().getFeatureGroup()).isEqualTo(obtainCategory.getFeatureGroups().get(0));
        }
    }
}
