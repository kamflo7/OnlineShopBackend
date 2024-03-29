package pl.kflorczyk.onlineshopbackend.services_and_repositories;

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
import pl.kflorczyk.onlineshopbackend.model.CompanyAddress;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.model.UserAddress;
import pl.kflorczyk.onlineshopbackend.repositories.UserRepository;
import pl.kflorczyk.onlineshopbackend.services.JwtService;
import pl.kflorczyk.onlineshopbackend.services.UserService;
import pl.kflorczyk.onlineshopbackend.validators.EmailValidator;
import pl.kflorczyk.onlineshopbackend.validators.UserAddressValidator;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;


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
        userService = new UserService(userRepository, passwordEncoder, new EmailValidator(), new UserAddressValidator(), new JwtService(userRepository));
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

    @Test
    public void editAddressTest() {
        User user = userService.registerUser("john.doe@gmail.com", "123456#^47");

        PersonAddressDTO personDTO = new PersonAddressDTO(122, "32-061", "London", "500100200", "John", "Doe");
        userService.createUserAddress(user.getID(), personDTO);

        UserAddress justPersisted = user.getAddresses().get(0);

        CompanyAddressDTO edit = new CompanyAddressDTO(244, "64-122", "Vandenberg", "333666999", "Something LTD", "1234567891");
        userService.editUserAddress(user.getID(), justPersisted.getID(), edit);

        CompanyAddress edited = (CompanyAddress) user.getAddresses().get(0);

        assertThat(user.getAddresses().size()).isEqualTo(1);
        assertThat(edited.getHouseNumber()).isEqualTo(edit.getHouseNumber());
        assertThat(edited.getZipCode()).isEqualTo(edit.getZipCode());
        assertThat(edited.getCity()).isEqualTo(edit.getCity());
        assertThat(edited.getPhoneNumber()).isEqualTo(edit.getPhoneNumber());
        assertThat(edited.getID()).isGreaterThan(justPersisted.getID());
    }

    @Test
    public void setDefaultAddress() {
        User user = userService.registerUser("john.doe@gmail.com", "123456#^47");

        PersonAddressDTO personDTO = new PersonAddressDTO(122, "32-061", "London", "500100200", "John", "Doe");
        userService.createUserAddress(user.getID(), personDTO);

        CompanyAddressDTO companyDTO = new CompanyAddressDTO(244, "64-122", "Vandenberg", "333666999", "Something LTD", "1234567891");
        userService.createUserAddress(user.getID(), companyDTO);

        CompanyAddressDTO companyDTO2 = new CompanyAddressDTO(488, "28-362", "Los Santos", "888999555", "Nothing LTD", "1234567891");
        userService.createUserAddress(user.getID(), companyDTO2);

        userService.setDefaultAddress(user.getID(), user.getAddresses().get(2).getID());

        int i = 0;
        for(UserAddress a : user.getAddresses())
            assertThat((++i == 3) == a.isDefault()).isTrue();
    }
}
