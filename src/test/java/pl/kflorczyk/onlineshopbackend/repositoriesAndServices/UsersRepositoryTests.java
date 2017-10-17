package pl.kflorczyk.onlineshopbackend.repositoriesAndServices;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.model.User;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UsersRepositoryTests {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private TestEntityManager entityManager;

    @Test
    public void insertTest() {
        User user = new User();
        user.setEmail("johndoe@example.com");
        user.setPassword("foobar123");

        userRepository.save(user);
        userRepository.flush();

        User obtain = userRepository.findByEmail("johndoe@example.com");
        assertThat(user.getEmail()).isEqualTo(obtain.getEmail());
    }
}
