package pl.kflorczyk.onlineshopbackend.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.dto.OrderProductDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.ProductNotFoundException;
import pl.kflorczyk.onlineshopbackend.exceptions.UserAddressNotFoundException;
import pl.kflorczyk.onlineshopbackend.exceptions.UserNotFoundException;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    @NonNull private final OrderRepository orderRepository;
    @NonNull private final UserService userService;
    @NonNull private final ProductService productService;

    public Order makeOrder(long addressID,
                           String deliveryMethod, String paymentMethod,
                           List<OrderProductDTO> productsDTO) {

        User user = userService.getCurrentUser();
        if(user == null) {
            throw new UserNotFoundException("User not found");
        }

        Optional<UserAddress> address = user.getAddresses().stream().filter(a -> a.getID() == addressID).findAny();
        if(!address.isPresent()) {
            throw new UserAddressNotFoundException("UserAddress not found for given ID");
        }

        List<OrderProduct> orderProducts = new ArrayList<>(productsDTO.size());
        for(OrderProductDTO dto : productsDTO) {
            Product product = productService.getProduct(dto.getProductID());
            if(product == null) {
                throw new ProductNotFoundException("Product not found for given ID");
            }

            OrderProduct orderProduct = new OrderProduct(product, product.getPrice(), dto.getAmount());
            orderProducts.add(orderProduct);
        }

        Order order = new Order(new Date(), user, address.get(), deliveryMethod, paymentMethod);
        order.addOrderProducts(orderProducts);
        orderRepository.saveAndFlush(order);
        return order;
    }

    public List<Order> getOrders() {
        User user = userService.getCurrentUser();

        if(user == null) {
            throw new UserNotFoundException("User not found for given ID");
        }

        return orderRepository.findByUserOrderByIDDesc(user);
    }

    public Order getOrder(long orderID) {
        Order order = orderRepository.findOne(orderID);
        User user = userService.getCurrentUser();

        if(order.getUser().getID() == user.getID() || user.isAdmin()) {
            return order;
        }

        return null;
    }
}
