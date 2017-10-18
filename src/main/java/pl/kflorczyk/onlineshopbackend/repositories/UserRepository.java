package pl.kflorczyk.onlineshopbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kflorczyk.onlineshopbackend.model.User;

//@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); //    @Query("FROM User WHERE login = :login")
}
