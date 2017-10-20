package pl.kflorczyk.onlineshopbackend.servicesAndRepositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.dto.CompanyAddressDTO;
import pl.kflorczyk.onlineshopbackend.dto.PersonAddressDTO;
import pl.kflorczyk.onlineshopbackend.dto.UserAddressDTO;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.model.UserAddress;
import pl.kflorczyk.onlineshopbackend.repositories.UserRepository;
import pl.kflorczyk.onlineshopbackend.services.UserService;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceAndRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @Before
    public void setup() {
        userService = new UserService(userRepository, passwordEncoder);
        passwordEncoder = new BCryptPasswordEncoder(11);
    }

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

    @Test
    public void addAddressTest() {
        User user = userService.registerUser("john.doe@gmail.com", "123456#^47");

        PersonAddressDTO personDTO = new PersonAddressDTO(122, "32-061", "London", "500100200", "John", "Doe");
        userService.createUserAddress(user.getID(), personDTO);

        CompanyAddressDTO companyDTO = new CompanyAddressDTO(150, "32-061", "London", "722360987", "Travel LTD", "1234567890");
        userService.createUserAddress(user.getID(), companyDTO);

        List<UserAddress> addresses = user.getAddresses();
        assertThat(addresses.size()).isEqualTo(2);
    }
}
