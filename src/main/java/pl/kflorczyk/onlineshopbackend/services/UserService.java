package pl.kflorczyk.onlineshopbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void createUser(User user) {
        userRepository.saveAndFlush(user);
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }
}
