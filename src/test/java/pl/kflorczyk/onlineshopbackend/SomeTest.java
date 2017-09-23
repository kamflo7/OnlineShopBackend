package pl.kflorczyk.onlineshopbackend;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.config.SecurityConfig;
import pl.kflorczyk.onlineshopbackend.jwt_authentication.JwtAuthFilter;
import pl.kflorczyk.onlineshopbackend.jwt_authentication.JwtAuthenticationProvider;
import pl.kflorczyk.onlineshopbackend.model.Product;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.repository.ProductRepository;
import pl.kflorczyk.onlineshopbackend.repository.UserRepository;
import pl.kflorczyk.onlineshopbackend.services.JwtService;
import pl.kflorczyk.onlineshopbackend.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(classes = {SecurityConfig.class, JwtAuthFilter.class, JwtAuthenticationProvider.class, JwtService.class})
public class SomeTest {

//    @TestConfiguration
//    static class EmployeeServiceImplTestContextConfiguration {
//
//        @Bean
//        public UserService userService() {
//            return new UserService();
//        }
//    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

//    @MockBean
//    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        User user = new User();
        user.setEmail("andrzej@gmail.com");

        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(user);
    }

    @Test
    public void jakisTest() {
        String email = "andrzej@gmail.com";
        User found = userService.getUser(email);

        Assert.assertEquals(email, found.getEmail());
    }
}
