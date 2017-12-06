package pl.kflorczyk.onlineshopbackend.services_and_repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kflorczyk.onlineshopbackend.dto.OrderProductDTO;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.repositories.OrderRepository;
import pl.kflorczyk.onlineshopbackend.services.OrderService;
import pl.kflorczyk.onlineshopbackend.services.ProductService;
import pl.kflorczyk.onlineshopbackend.services.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderTests {

    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    @Before
    public void setup() {
        orderService = new OrderService(orderRepository, userService, productService);
    }

    @Test
    public void makeOrderTest() {
        User user = new User("john.doe@gmail.com", "123456");
        Mockito.when(userService.getUser(1L)).thenReturn(user);

        UserAddress address = new PersonAddress("John", "Doe");
        address.setID(2L);
        address.setAddressName("Main");
        address.setCity("Las Vegas");
        address.setDefault(true);
        address.setHouseNumber(123);
        address.setPhoneNumber("500300100");
        address.setStreet("Woods Avenue");
        address.setZipCode("11-570");
        user.addAddress(address);

        CategoryLogic smartphones = new CategoryLogic("Smartphones");
        Product productId10 = new Product("Samsung Galaxy S8", new BigDecimal("4000.00"), 100,
                "Description about s8", smartphones);
        Product productId11 = new Product("Xiaomi Mi A1", new BigDecimal("1200.00"), 100,
                "Description about Mi A1", smartphones);
        Mockito.when(productService.getProduct(10)).thenReturn(productId10);
        Mockito.when(productService.getProduct(11)).thenReturn(productId11);

        List<OrderProductDTO> productsDTO = new ArrayList<>();
        productsDTO.add(new OrderProductDTO(10, 1));
        productsDTO.add(new OrderProductDTO(11, 1));
        Order order = null;
        try {
            order = orderService.makeOrder(1L, "Courier", "In advance", productsDTO);
        } catch(Exception e) {
            fail("Failed because caught exception ");
        }

        assertThat(order.getOrderProducts().size()).isEqualTo(2);
    }
}
