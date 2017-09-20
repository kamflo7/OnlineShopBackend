package pl.kflorczyk.onlineshopbackend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.kflorczyk.onlineshopbackend.model.User;

//@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email); //    @Query("FROM User WHERE login = :login")
}
