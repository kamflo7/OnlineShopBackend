package pl.kflorczyk.onlineshopbackend.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryLogicRepositoryTests {

    @Autowired
    private CategoryLogicRepository repository;

    @Test
    public void insertTest() {
        CategoryLogic categoryLogic = new CategoryLogic();
        categoryLogic.setName("Smartphones");

        repository.save(categoryLogic);
        repository.flush();

        System.out.println("CategoryLogic id: " + categoryLogic.getID());

        CategoryLogic obtain = repository.findByName("Smartphones");
        assertThat(obtain.getName()).isEqualTo(categoryLogic.getName());
    }
}
