package pl.kflorczyk.onlineshopbackend.repositoriesAndServices;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.product_filters.FilterParameters;
import pl.kflorczyk.onlineshopbackend.services.ProductService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryLogicRepository categoryLogicRepository;

    private ProductService productService;

    private void setupDatabaseContent() {
        String[] phoneNames =   new String[] { "Samsung Galaxy Note 8", "Huawei P8 Lite", "Samsung Galaxy A3 (2017)", "Huawei Honor 4X", "Huawei Honor 8" };
        String[] prices =       new String[] { "4299.0", "801.61", "1198.0", "1034.49", "1499.0" };

        FeatureValue[] ram =    new FeatureValue[] { ram6GB, ram2GB, ram2GB, ram2GB, ram4GB};
        FeatureValue[] inches = new FeatureValue[] { screen63, screen5, screen47, screen55, screen52};
        FeatureValue[] inchesR= new FeatureValue[] { screenRange6plus, screenRange48_54, screenRange41_47, screenRange55_6, screenRange48_54};
        FeatureValue[] res =    new FeatureValue[] { res2960x1440, res1280x720, res1280x720, res1280x720, res1920x1080};

        List<List<FeatureValue>> cn= Lists.newArrayList(
                Lists.newArrayList(connNfc, connBt42, connWifi),
                Lists.newArrayList(connBt42),
                Lists.newArrayList(connWifi),
                Lists.newArrayList(connBt42, connWifi),
                Lists.newArrayList(connNfc, connWifi)
        );

        for(int i=0; i<phoneNames.length; i++) {
            Product p = new Product(phoneNames[i], new BigDecimal(prices[i]), 100, "Some description index " + i, categoryLogicSmartphones);
            p.addFeature(new FeatureBag(featureDefRAM, ram[i]));
            p.addFeature(new FeatureBag(featureDefScreenInches, inches[i]));
            p.addFeature(new FeatureBag(featureDefScreenInchesRange, inchesR[i]));
            p.addFeature(new FeatureBag(featureDefResolution, res[i]));
            p.addFeature(new FeatureBag(featureDefConnection, cn.get(i)));
            productRepository.save(p);
        }
        productRepository.flush();
    }

    private CategoryLogic categoryLogicSmartphones;
    private FeatureGroup featureGroupTechInfo;
    private FeatureDefinition featureDefCpu, featureDefRAM, featureDefInternalStorage, featureDefScreenInches, featureDefScreenInchesRange, featureDefResolution, featureDefConnection;
    private FeatureValue    ram2GB, ram3GB, ram4GB, ram6GB, storage8GB, storage16GB, storage32GB, screen63, screen5, screen52, screen55, screen47, screenRange41_47, screenRange48_54, screenRange55_6, screenRange6plus,
                            res2960x1440, res1280x720, res1920x1080,
                            connWifi, connBt42, connNfc;

    public void setupDatabaseStructure() {
        categoryLogicSmartphones = new CategoryLogic("Smartfony");
        featureGroupTechInfo = new FeatureGroup("Informacje techniczne");

        featureDefCpu = new FeatureDefinition("Procesor", featureGroupTechInfo);
        featureDefRAM = new FeatureDefinition("Pamięć RAM", featureGroupTechInfo, true);
        featureDefInternalStorage = new FeatureDefinition("Pamięć wewnętrzna", featureGroupTechInfo, true);
        featureDefScreenInches = new FeatureDefinition("Przekątna ekranu", featureGroupTechInfo);
        featureDefScreenInchesRange = new FeatureDefinition("Przekątna ekranu", featureGroupTechInfo, true);
        featureDefResolution = new FeatureDefinition("Rozdzielczość ekranu", featureGroupTechInfo, true);
        featureDefConnection = new FeatureDefinition("Łączność bezprzewodowa", featureGroupTechInfo, true, true);

        ram2GB = new FeatureValue("2GB"); ram3GB = new FeatureValue("3GB"); ram4GB = new FeatureValue("4GB"); ram6GB = new FeatureValue("6GB");
        storage8GB = new FeatureValue("8GB"); storage16GB = new FeatureValue("16GB"); storage32GB = new FeatureValue("32GB");
        screen63 = new FeatureValue("6.3"); screen5 = new FeatureValue("5"); screen52 = new FeatureValue("5.2"); screen47 = new FeatureValue("4.7"); screen55 = new FeatureValue("5.5");
        screenRange41_47 = new FeatureValue("4.1\" - 4.7\"") ;screenRange48_54 = new FeatureValue("4.8\" - 5.4\""); screenRange55_6 = new FeatureValue("5.5\" - 6\""); screenRange6plus = new FeatureValue("ponad 6\"");
        res2960x1440 = new FeatureValue("2960x1440"); res1280x720 = new FeatureValue("1280x720"); res1920x1080 = new FeatureValue("1920x1080");
        connWifi = new FeatureValue("WiFi"); connBt42 = new FeatureValue("Bluetooth v4.2"); connNfc = new FeatureValue("NFC");

        featureDefRAM.setFeatureValueDefinitions(Lists.newArrayList(ram2GB, ram3GB, ram4GB));
        featureDefInternalStorage.setFeatureValueDefinitions(Lists.newArrayList(storage8GB, storage16GB, storage32GB));
        featureDefScreenInches.setFeatureValueDefinitions(Lists.newArrayList(screen47, screen5, screen63));
        featureDefScreenInchesRange.setFeatureValueDefinitions(Lists.newArrayList(screenRange48_54, screenRange55_6, screen63));
        featureDefConnection.setFeatureValueDefinitions(Lists.newArrayList(connWifi, connNfc, connBt42));

        categoryLogicSmartphones.addFeatureGroup(featureGroupTechInfo);
        categoryLogicSmartphones.addFeatureDefinition(featureDefCpu);
        categoryLogicSmartphones.addFeatureDefinition(featureDefRAM);
        categoryLogicSmartphones.addFeatureDefinition(featureDefInternalStorage);
        categoryLogicSmartphones.addFeatureDefinition(featureDefScreenInches);
        categoryLogicSmartphones.addFeatureDefinition(featureDefScreenInchesRange);
        categoryLogicSmartphones.addFeatureDefinition(featureDefResolution);
        categoryLogicSmartphones.addFeatureDefinition(featureDefConnection);

        categoryLogicRepository.saveAndFlush(categoryLogicSmartphones);
    }

    @Test
    public void shouldReturnFilteredProducts() {
        setupDatabaseStructure();
        setupDatabaseContent();

        productService = new ProductService(productRepository, categoryLogicRepository);

        // pure filter params in URL
        String urlParams = String.format("%d=%d,%d=%d.%d.%d",
                featureDefRAM.getId(), ram6GB.getID(),
                featureDefConnection.getId(), connWifi.getID(), connBt42.getID(), connNfc.getID());

        FilterParameters filterParameters = new FilterParameters(urlParams);
        List<Product> all = productService.getProducts(categoryLogicSmartphones, filterParameters);

        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getFeatureBags().size()).isEqualTo(5);
    }

    @Test
    public void shouldReturnAllProductsInCategory() {
        setupDatabaseStructure();
        setupDatabaseContent();

        CategoryLogic c = CategoryLogic.ofID(categoryLogicSmartphones.getID());
        List<Product> byCategoryLogic = productRepository.findByCategoryLogic(c);
        assertThat(byCategoryLogic.size()).isGreaterThan(0);
    }

    @Test
    public void shouldCreateProduct() {
        setupDatabaseStructure();
        productService = new ProductService(productRepository, categoryLogicRepository);

        Map<Long, List<Long>> features = new HashMap<>();
        features.put(featureDefRAM.getId(), Lists.newArrayList(ram4GB.getID()));
        features.put(featureDefInternalStorage.getId(), Lists.newArrayList(storage32GB.getID()));
        features.put(featureDefScreenInches.getId(), Lists.newArrayList(screen63.getID()));
        features.put(featureDefConnection.getId(), Lists.newArrayList(connWifi.getID(), connBt42.getID(), connNfc.getID()));

        try {
            productService.createProduct(categoryLogicSmartphones.getID(),
                    "Huawei Mate 4", "Najnowszy telefon z premiera w pazdzierniku 2017 (chyba)",
                    new BigDecimal("1980.0"), 15, features);
        } catch(RuntimeException e) {
            fail("Should not throw runtimeexception: " + e.getMessage());
        }

        Product obtained = productService.getProducts(categoryLogicSmartphones).get(0);

        assertThat(obtained.getName()).isEqualTo("Huawei Mate 4");
        assertThat(obtained.getCategoryLogic().getID()).isEqualTo(categoryLogicSmartphones.getID());
        assertThat(obtained.getFeatureBags().size()).isEqualTo(4);
    }
}
