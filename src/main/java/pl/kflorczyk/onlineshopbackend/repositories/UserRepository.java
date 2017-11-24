package pl.kflorczyk.onlineshopbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kflorczyk.onlineshopbackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
