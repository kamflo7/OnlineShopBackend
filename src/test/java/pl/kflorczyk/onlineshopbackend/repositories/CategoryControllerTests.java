package pl.kflorczyk.onlineshopbackend.repositories;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.dto.FeatureDefinitionDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.InvalidFeatureValueDefinitionException;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.FeatureDefinition;
import pl.kflorczyk.onlineshopbackend.model.FeatureGroup;
import pl.kflorczyk.onlineshopbackend.model.FeatureValue;
import pl.kflorczyk.onlineshopbackend.services.CategoryService;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryControllerTests {

    @Autowired
    private CategoryLogicRepository categoryLogicRepository;

    @Autowired
    private CategoryViewRepository categoryViewRepository;

    private CategoryService categoryService;

    @Before
    public void setup() {
        categoryService = new CategoryService(categoryViewRepository, categoryLogicRepository);
    }

    @Test
    public void shouldCreateCategoryAndReturnNullForAttemptToCreateExistCategory() {
        CategoryLogic smartfony = categoryService.createNewCategory("Smartfony");
        CategoryLogic smartfony2 = categoryService.createNewCategory("Smartfony");
        CategoryLogic smartfony3 = categoryService.createNewCategory("smartfony");

        assertThat(smartfony).isNotNull();
        assertThat(smartfony2).isNull();
        assertThat(smartfony3).isNull();
    }

    @Test
    public void shouldCreateFeatureGroupForCategory() {
        CategoryLogic smartfony = categoryService.createNewCategory("Smartfony");
        long id = smartfony.getID();

        categoryService.createFeatureGroup("Informacje techniczne", smartfony);
        FeatureGroup obtained = categoryService.getCategoriesLogic().get(0).getFeatureGroups().get(0);
        assertThat(obtained.getName()).isEqualTo("Informacje techniczne");
    }

    @Test
    public void shouldCreateFeatureDefinitionsAndTheirPotentialValuesForCategory() {
        CategoryLogic smartfony = categoryService.createNewCategory("Smartfony");
        categoryService.createFeatureGroup("Informacje techniczne", smartfony);
        FeatureGroup informacje_techniczne = smartfony.getFeatureGroups().stream().filter(g -> g.getName().equals("Informacje techniczne")).findAny().get();

        FeatureDefinitionDTO featureDefinitionDTO = new FeatureDefinitionDTO(false, true, true, "Pamięć RAM",
                Lists.newArrayList("512MB", "1GB", "2GB", "3GB", "4GB"));
        try {
            categoryService.createFeatureDefinition(featureDefinitionDTO, informacje_techniczne, smartfony);
        } catch (InvalidFeatureValueDefinitionException e) {
            fail("createFeatureDefinition method should not throw exception");
        }

        //
        CategoryLogic smartfonyObtained = categoryService.getCategoryLogic("Smartfony");
        List<FeatureDefinition> featureDefinitionsObtained = smartfonyObtained.getFeatureDefinitions();

        assertThat(featureDefinitionsObtained.get(0).getFeatureValueDefinitions().size()).isEqualTo(featureDefinitionDTO.getValues().size());
        assertThat(featureDefinitionsObtained.get(0).getFeatureValueDefinitions().get(0).getValue()).isEqualTo(featureDefinitionDTO.getValues().get(0));
    }
}
