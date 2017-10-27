package pl.kflorczyk.onlineshopbackend.servicesAndRepositories;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.dto.FeatureBagDTO;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTO;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTOEditable;
import pl.kflorczyk.onlineshopbackend.dto.ProductDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.product_filters.FilterParameters;
import pl.kflorczyk.onlineshopbackend.repositories.CategoryLogicRepository;
import pl.kflorczyk.onlineshopbackend.repositories.CategoryViewRepository;
import pl.kflorczyk.onlineshopbackend.repositories.ProductRepository;
import pl.kflorczyk.onlineshopbackend.services.CategoryService;
import pl.kflorczyk.onlineshopbackend.services.ProductService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductAndCategoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryLogicRepository categoryLogicRepository;

    @Autowired
    private CategoryViewRepository categoryViewRepository;

    private ProductService productService;
    private CategoryService categoryService;

    @Before
    public void setup() {
        categoryService = new CategoryService(categoryViewRepository, categoryLogicRepository);
    }

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
        featureDefResolution.setFeatureValueDefinitions(Lists.newArrayList(res1920x1080, res1280x720, res2960x1440));

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

    @Test
    public void createCategory_and_preventFromCreateExistingName() {
        CategoryLogic smartfony = categoryService.createNewCategory("Smartfony");
        int exceptions = 0;

        try {
            CategoryLogic smartfony2 = categoryService.createNewCategory("Smartfony");
        } catch(CategoryAlreadyExistsException e) {
            exceptions++;   // not found opposite method to junit.fail()
        }

        try {
            CategoryLogic smartfony3 = categoryService.createNewCategory("smartfony");
        } catch(CategoryAlreadyExistsException e) {
            exceptions++;
        }


        assertThat(exceptions).isEqualTo(2);
        assertThat(smartfony).isNotNull();
    }

    @Test
    public void editCategoryName() {
        CategoryLogic smartfony = categoryService.createNewCategory("Smartfony");
        CategoryLogic edited = null;
        try {
            edited = categoryService.editCategoryLogic(smartfony.getID(), "Inne smartfony");
        } catch(InvalidCategoryNameException | CategoryNotFoundException e) {
            fail("Should not appear this");
        }

        assertThat(edited.getName()).isEqualTo("Inne smartfony");
    }

    @Test
    public void createFeatureGroup() {
        CategoryLogic smartfony = categoryService.createNewCategory("Smartfony");
        long id = smartfony.getID();

        categoryService.createFeatureGroup("Informacje techniczne", smartfony);
        FeatureGroup obtained = categoryService.getCategoriesLogic().get(0).getFeatureGroups().get(0);
        assertThat(obtained.getName()).isEqualTo("Informacje techniczne");
    }

    @Test
    public void editFeatureGroup() {
        CategoryLogic smartfony = categoryService.createNewCategory("Smartfony");

        categoryService.createFeatureGroup("Informacje techniczne", smartfony.getID());
        FeatureGroup obtained = categoryService.getCategoriesLogic().get(0).getFeatureGroups().get(0);

        CategoryLogic updatedCategory = null;
        try {
            updatedCategory = categoryService.editFeatureGroup(smartfony.getID(), obtained.getId(), "Informacje bardzo techniczne");
        } catch(InvalidFeatureGroupNameException | CategoryNotFoundException | FeatureGroupNotFoundException e) {
            fail("Should not appear this");
        }

        assertThat(updatedCategory.getFeatureGroups().get(0).getName()).isEqualTo("Informacje bardzo techniczne");
    }

    @Test
    public void editFeatureDefinition() {
        CategoryLogic smartfony = categoryService.createNewCategory("Smartfony");
        categoryService.createFeatureGroup("Informacje techniczne", smartfony);
        FeatureGroup obtained = categoryService.getCategoriesLogic().get(0).getFeatureGroups().get(0);

        FeatureDefinitionDTO dto = new FeatureDefinitionDTO(true, true, true, "Pamiec RAM", Lists.newArrayList("1GB", "2GB", "3GB"));
        categoryService.createFeatureDefinition(dto, obtained.getId(), smartfony.getID());

        FeatureDefinition justCreated = smartfony.getFeatureDefinitions().get(0);
        List<FeatureValue> values = justCreated.getFeatureValueDefinitions();

        Map<Long, String> map = new HashMap<>(3);
        map.put(values.get(0).getID(), "1024MB");
        map.put(values.get(1).getID(), "2048MB");
        map.put(values.get(2).getID(), "3072MB");

        FeatureDefinitionDTOEditable dto2 = new FeatureDefinitionDTOEditable(true, true, true, "Pamiec RAM", false, map);
        dto2.setNewValues(Lists.newArrayList("4GB", "6GB"));

        categoryService.editFeatureDefinition(smartfony.getID(), obtained.getId(), justCreated.getId(), dto2);

        assertThat(smartfony.getFeatureDefinitions().get(0).getFeatureValueDefinitions().size()).isEqualTo(5);

        String[] expectedValues = new String[] {"1024MB", "2048MB", "3072MB", "4GB", "6GB"};
        for(int i=0; i<5; i++)
            assertThat(smartfony.getFeatureDefinitions().get(0).getFeatureValueDefinitions().get(i).getValue()).isEqualTo(expectedValues[i]);
    }

    @Test
    public void createFeatureDefinitionWithItsValues() {
        CategoryLogic smartfony = categoryService.createNewCategory("Smartfony");
        categoryService.createFeatureGroup("Informacje techniczne", smartfony);
        FeatureGroup informacje_techniczne = smartfony.getFeatureGroups().stream().filter(g -> g.getName().equals("Informacje techniczne")).findAny().get();

        FeatureDefinitionDTO featureDefinitionDTO = new FeatureDefinitionDTO(false, true, true, "Pamięć RAM",
                Lists.newArrayList("512MB", "1GB", "2GB", "3GB", "4GB"));

        boolean shouldFail = false;

        try {
            categoryService.createFeatureDefinition(featureDefinitionDTO, informacje_techniczne, smartfony);
        } catch (InvalidFeatureValueDefinitionException e) {
            fail("createFeatureDefinition method should not throw exception");
        }

        try {
            categoryService.createFeatureDefinition(featureDefinitionDTO, informacje_techniczne, smartfony);
        } catch (FeatureDefinitionAlreadyExists e) {
            shouldFail = true;
        }

        //
        CategoryLogic smartfonyObtained = categoryService.getCategoryLogic("Smartfony");
        List<FeatureDefinition> featureDefinitionsObtained = smartfonyObtained.getFeatureDefinitions();

        assertThat(featureDefinitionsObtained.get(0).getFeatureValueDefinitions().size()).isEqualTo(featureDefinitionDTO.getNewValues().size());
        assertThat(featureDefinitionsObtained.get(0).getFeatureValueDefinitions().get(0).getValue()).isEqualTo(featureDefinitionDTO.getNewValues().get(0));
        assertThat(shouldFail).isTrue();
    }

    @Test
    public void getProducts_withGivenFilterParameters() {
        setupDatabaseStructure();
        setupDatabaseContent();

        productService = new ProductService(productRepository, categoryLogicRepository);

        // pure filter params in URL
        String urlParams = String.format("%d=%d,%d=%d.%d.%d,prc=100.0-5000.0",
                featureDefRAM.getId(), ram6GB.getID(),
                featureDefConnection.getId(), connWifi.getID(), connBt42.getID(), connNfc.getID());

//        urlParams = "";

        FilterParameters filterParameters = new FilterParameters(urlParams);
        List<Product> all = productService.getProducts(categoryLogicSmartphones, filterParameters);

        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getFeatureBags().size()).isEqualTo(5);
    }

    @Test
    public void createProduct() {
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

        assertThat(obtained.getCategoryLogic().getID()).isEqualTo(categoryLogicSmartphones.getID());
        assertThat(obtained.getFeatureBags().size()).isEqualTo(4);

        ProductTest test = new ProductTest(obtained);
        assertThat(test.has(featureDefRAM, ram4GB)).isTrue();
        assertThat(test.has(featureDefInternalStorage, storage32GB)).isTrue();
        assertThat(test.has(featureDefScreenInches, screen63)).isTrue();
        assertThat(test.has(featureDefConnection, connWifi, connBt42, connNfc)).isTrue();
        assertThat(obtained.getName()).isEqualTo("Huawei Mate 4");
        assertThat(obtained.getDescription()).isEqualTo("Najnowszy telefon z premiera w pazdzierniku 2017 (chyba)");
        assertThat(obtained.getAmount()).isEqualTo(15);
        assertThat(obtained.getPrice().compareTo(new BigDecimal("1980.0"))).isEqualTo(0);
    }

    @Test
    public void editProduct() {
        setupDatabaseStructure();
        productService = new ProductService(productRepository, categoryLogicRepository);

        ProductDTO givenCreatingDTO = new ProductDTO("Huawei Testowy", "Spoko telefon", new BigDecimal("1000.00"), 100);
        Map<Long, List<Long>> givenCreatingFeatures = new HashMap<>();
        givenCreatingFeatures.put(featureDefRAM.getId(), Lists.newArrayList(ram2GB.getID()));
        givenCreatingFeatures.put(featureDefInternalStorage.getId(), Lists.newArrayList(storage8GB.getID()));
        givenCreatingFeatures.put(featureDefConnection.getId(), Lists.newArrayList(connWifi.getID(), connBt42.getID(), connNfc.getID()));
        givenCreatingFeatures.put(featureDefResolution.getId(), Lists.newArrayList(res1920x1080.getID()));
        givenCreatingDTO.setFeatures(givenCreatingFeatures);

        Product createdProduct = null;
        try {
            createdProduct = productService.createProduct(categoryLogicSmartphones.getID(), givenCreatingDTO);
        }  catch(RuntimeException e) {
            fail("Should not throw RuntimeException [creating]: " + e.getMessage());
        }

        // edit:
        ProductDTO givenEditDTO = new ProductDTO();
        givenEditDTO.setDescription("Zmiana opisu telefonu");
        givenEditDTO.setPrice(new BigDecimal("2000.00"));
        Map<Long, List<Long>> givenEditFeatures = new HashMap<>();
        givenEditFeatures.put(featureDefRAM.getId(), Lists.newArrayList(ram4GB.getID()));
        givenEditFeatures.put(featureDefInternalStorage.getId(), Lists.newArrayList(storage16GB.getID()));
        givenEditDTO.setFeatures(givenEditFeatures);

        Product editedProduct = null;
        try {
            editedProduct = productService.editProduct(createdProduct.getID(), givenEditDTO);
        }  catch(RuntimeException e) {
            fail("Should not throw RuntimeException [editing]: " + e.getMessage());
        }

        ProductTest test = new ProductTest(editedProduct);
        assertThat(test.has(featureDefRAM, ram4GB)).isTrue();
        assertThat(test.has(featureDefInternalStorage, storage16GB)).isTrue();
        assertThat(test.has(featureDefConnection, connWifi, connBt42, connNfc)).isTrue();
        assertThat(test.has(featureDefResolution, res1920x1080)).isTrue();
        assertThat(editedProduct.getDescription()).isEqualTo("Zmiana opisu telefonu");
        assertThat(editedProduct.getPrice().compareTo(new BigDecimal("2000.00"))).isEqualTo(0);
    }

    @Test
    public void shouldCorrectlyValidateAndTranslateGivenLongIndexesToBussinessLogic() {
        setupDatabaseStructure();
        productService = new ProductService(productRepository, categoryLogicRepository);

        Map<Long, List<Long>> setupFeatures = new HashMap<>();
        setupFeatures.put(featureDefRAM.getId(), Lists.newArrayList(ram2GB.getID()));
        setupFeatures.put(featureDefInternalStorage.getId(), Lists.newArrayList(storage8GB.getID()));
        setupFeatures.put(featureDefConnection.getId(), Lists.newArrayList(connWifi.getID(), connBt42.getID(), connNfc.getID()));
        setupFeatures.put(featureDefResolution.getId(), Lists.newArrayList(res1920x1080.getID()));

        List<FeatureBagDTO> converted = productService.convertProductRawDataToFeatureBagDTO(categoryLogicSmartphones, setupFeatures);
        List<FeatureDefinition> featureDefinitions = categoryLogicSmartphones.getFeatureDefinitions();

        int matchedDefs = 0;

        for(FeatureBagDTO bag : converted) {
            for(FeatureDefinition fd : featureDefinitions) {
                if(bag.getFeatureDefinition().getId() == fd.getId()) {
                    matchedDefs++;

                    int matchedValues = 0;
                    int needValuesToMatch = setupFeatures.get(fd.getId()).size();

                    for(FeatureValue fv : bag.getFeatureValues()) {
                        for(FeatureValue realFeatureValue : fd.getFeatureValueDefinitions()) {
                            if(realFeatureValue.getID() == fv.getID())
                                matchedValues++;
                        }
                    }

                    assertThat(matchedValues).isEqualTo(needValuesToMatch);
                }
            }
        }

        assertThat(matchedDefs).isEqualTo(setupFeatures.size());
    }

    private class ProductTest {
        private Product product;

        public ProductTest(Product product) {
            this.product = product;
        }

        public boolean has(FeatureDefinition fd, FeatureValue... fvs) {
            boolean featureDefFound = false;

            List<FeatureBag> featureBags = product.getFeatureBags();
            for(FeatureBag bag : featureBags) {
                if(bag.getFeatureDefinition().getId() == fd.getId()) {
                    int foundCount = 0;
                    featureDefFound = true;

                    for(FeatureValue fv : fvs) {
                        for(FeatureValue realFV : bag.getFeatureValues()) {
                            if(fv.getID() == realFV.getID())
                                foundCount++;
                        }
                    }

                    if(foundCount != fvs.length)
                        return false;
                }
            }

            return featureDefFound;
        }
    }
}
