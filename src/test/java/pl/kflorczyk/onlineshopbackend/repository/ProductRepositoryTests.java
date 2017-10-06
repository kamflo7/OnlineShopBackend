package pl.kflorczyk.onlineshopbackend.repository;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.filter_products.AlternativeParameter;
import pl.kflorczyk.onlineshopbackend.filter_products.FilterParameter;
import pl.kflorczyk.onlineshopbackend.filter_products.RangeParameter;
import pl.kflorczyk.onlineshopbackend.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    private List<Product> getSomeSmartphones(CategoryLogic categoryLogic) {
        FeatureDefinition featureDefRAM = null, featureDefScreenInches = null, featureDefResolution = null;

        for(FeatureDefinition def : categoryLogic.getFeatureDefinitions()) {
            if(def.getName().equals("Pamięć RAM")) featureDefRAM = def;
            else if(def.getName().equals("Przekątna ekranu")) featureDefScreenInches = def;
        }

        String[] phoneNames =   new String[] { "Xiaomi Redmi 4X", "Samsung Galaxy A3", "iPhone 7", "Huawei Honor 8" };
        String[] prices =       new String[] { "749", "1198", "2708", "1499" };
        String[] ram =          new String[] { "2GB", "3GB",  "2GB", "4GB" };
        String[] inches =       new String[] { "5", "4.7", "4.7", "5.2" };
        String[] resolutions =  new String[] { "1280x720", "1280x720", "1334x750", "1920x1080" };

        List<Product> list = new ArrayList<>();

        for(int i=0; i<phoneNames.length; i++) {
            Product p = new Product(phoneNames[i], new BigDecimal(prices[i]), 100, "Some description index " + i);
            p.addFeature(new Feature(featureDefRAM, ram[i]));
            p.addFeature(new Feature(featureDefScreenInches, inches[i]));
            p.addFeature(new Feature(featureDefResolution, resolutions[i]));
            list.add(p);
        }
        return list;
    }

    @Test
    public void filterTest() {
        CategoryLogic categoryLogicSmartphones = new CategoryLogic("Smartfony");
        FeatureGroup featureGroup = new FeatureGroup("Informacje techniczne");

        FeatureDefinition featureDefCpu = new FeatureDefinition("Procesor", featureGroup);
        FeatureDefinition featureDefRAM = new FeatureDefinition("Pamięć RAM", featureGroup);
        FeatureDefinition featureDefInternalStorage = new FeatureDefinition("Pamięć wewnętrzna", featureGroup);
        FeatureDefinition featureDefScreenInches = new FeatureDefinition("Przekątna ekranu", featureGroup);
        FeatureDefinition featureDefResolution = new FeatureDefinition("Rozdzielczość ekranu", featureGroup);

        featureDefRAM.setFilter(new MultiChoiceFilter());
        featureDefScreenInches.setFilter(new NumberRangeFilter("3.6-4|4.1-4.7|4.8-5.4|5.5-6|6-+"));

        categoryLogicSmartphones.addFeatureGroup(featureGroup);
        categoryLogicSmartphones.addFeatureDefinition(featureDefCpu);
        categoryLogicSmartphones.addFeatureDefinition(featureDefRAM);
        categoryLogicSmartphones.addFeatureDefinition(featureDefInternalStorage);
        categoryLogicSmartphones.addFeatureDefinition(featureDefScreenInches);
        categoryLogicSmartphones.addFeatureDefinition(featureDefResolution);

        categoryLogicRepository.saveAndFlush(categoryLogicSmartphones);

        // EVERYTHING ABOVE IS NOT IMPORTANT, JUST SETTING UP DATABASE

        List<Product> products = getSomeSmartphones(categoryLogicSmartphones);
        List<Product> output = new ArrayList<>();

        // THIS IS IMPORTANT, FILTERING:
        // Translating URL filter parameters to Java Objects (array of wanted filters):
        final FilterParameter[] filterParameters = new FilterParameter[] {
                new RangeParameter(4.8f, 5.4f),
                new AlternativeParameter(new String[] { "2GB", "4GB" })
                // here may be zero or many filters
        };

        // And equivalent FeatureDefinitions for filters - later will be auto composed - FeatureDefinition knows its Filter
        // From URL for each param it is easy to associate appropriate FeatureDefinition and Filter, now it is only test
        final FeatureDefinition[] featureDefinitions = new FeatureDefinition[] {
                featureDefScreenInches,
                featureDefRAM
        };

        for(Product p : products) {
            boolean isSuitable = true;

            for(int i=0; i<filterParameters.length; i++) {
                FeatureDefinition featureDefinition = featureDefinitions[i];
                FilterParameter filterParameter = filterParameters[i];

                Feature productFeature = p.getFeatures().stream().filter(f -> f.getFeatureDefinition().getId() == featureDefinition.getId()).findFirst().get();
                if(!featureDefinition.getFilter().isSuitable(productFeature, filterParameter)) {
                    isSuitable = false;
                    break;
                }
            }

            if(isSuitable)
                output.add(p);
        }



        System.out.println("For breakpoing");

        //

//        CategoryLogic smartfony = categoryLogicRepository.findByName("Smartfony");
//        List<FeatureDefinition> obtainFeatureDefinitions = smartfony.getFeatureDefinitions();
//        assertThat(obtainFeatureDefinitions.get(0).getFilter().sayWhatever()).isEqualTo("Multi choice text");
//        assertThat(obtainFeatureDefinitions.get(1).getFilter().sayWhatever()).isEqualTo("Range text");
    }

    @Test
    public void relationTestBetweenCategoryLogicAndFeaturesAndProduct() {
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
