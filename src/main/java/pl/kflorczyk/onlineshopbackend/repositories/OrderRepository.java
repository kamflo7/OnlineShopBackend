package pl.kflorczyk.onlineshopbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kflorczyk.onlineshopbackend.model.Order;
import pl.kflorczyk.onlineshopbackend.model.User;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByIDDesc(User user);
}
