package pl.kflorczyk.onlineshopbackend.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.dto.CompanyAddressDTO;
import pl.kflorczyk.onlineshopbackend.dto.PersonAddressDTO;
import pl.kflorczyk.onlineshopbackend.dto.UserAddressDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.jwt_authentication.JwtAuthToken;
import pl.kflorczyk.onlineshopbackend.model.CompanyAddress;
import pl.kflorczyk.onlineshopbackend.model.PersonAddress;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.model.UserAddress;
import pl.kflorczyk.onlineshopbackend.repositories.UserRepository;
import pl.kflorczyk.onlineshopbackend.validators.EmailValidator;
import pl.kflorczyk.onlineshopbackend.validators.PasswordValidator;
import pl.kflorczyk.onlineshopbackend.validators.UserAddressValidator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @NonNull private UserRepository userRepository;
    @NonNull private PasswordEncoder passwordEncoder;

    @NonNull private EmailValidator emailValidator;
    @NonNull private UserAddressValidator userAddressValidator;

    @NonNull private JwtService jwtService;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        try {
            user = jwtService.verify((JwtAuthToken) authentication);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return user;
    }

    private boolean userExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User getUser(long id) {
        return userRepository.findOne(id);
    }

    public User registerUser(String email, String password) {
        if(email == null || password == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        if(userExists(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if(!emailValidator.validate(email)) {
            throw new InvalidEmailException("Invalid email");
        }

        if(!new PasswordValidator().validate(password)) {
            throw new InvalidPasswordException("Invalid password");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.saveAndFlush(user);
        return user;
    }

    public User loginUser(String email, String password) {
        if(email == null || password == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        User user = getUser(email);

        if(user == null) {
            throw new UserNotFoundException("There is no account with given email");
        }

        if(password == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordNotMatchException("Incorrect password");
        }

        return user;
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public void createUserAddress(long userID, UserAddressDTO addressDTO) {
        if(addressDTO == null) {
            throw new NullPointerException("UserAddressDTO is null");
        }

        if(!userAddressValidator.validate(addressDTO)) {
            throw new InvalidAddressException("Invalid address");
        }

        User user = getUser(userID);
        if(user == null) {
            throw new UserNotFoundException("User not found for given id");
        }

        UserAddress userAddress = null;
        if(addressDTO instanceof PersonAddressDTO) {
            userAddress = new PersonAddress((PersonAddressDTO) addressDTO);
        } else if(addressDTO instanceof CompanyAddressDTO) {
            userAddress = new CompanyAddress((CompanyAddressDTO) addressDTO);
        }

        user.addAddress(userAddress);
        userRepository.saveAndFlush(user);
    }

    public void editUserAddress(long userID, long addressID, UserAddressDTO addressDTO) {
        if(addressDTO == null) {
            throw new NullPointerException("UserAddressDTO is null");
        }

        if(!userAddressValidator.validate(addressDTO)) {
            throw new InvalidAddressException("Invalid address");
        }

        User user = getUser(userID);
        if(user == null) {
            throw new UserNotFoundException("User not found for given id");
        }

        Optional<UserAddress> persistingAddress = user.getAddresses().stream().filter(a -> a.getID() == addressID).findAny();
        if(!persistingAddress.isPresent()) {
            throw new UserAddressNotFoundException("UserAddress not found for given ID");
        }

        UserAddress userAddress = null;
        if(addressDTO instanceof PersonAddressDTO) {
            userAddress = new PersonAddress((PersonAddressDTO) addressDTO);
        } else if(addressDTO instanceof CompanyAddressDTO) {
            userAddress = new CompanyAddress((CompanyAddressDTO) addressDTO);
        }

        user.removeAddress(persistingAddress.get());
        user.addAddress(userAddress);
        userRepository.saveAndFlush(user);
    }

    public void setDefaultAddress(long userID, long addressID) {
        User user = getUser(userID);
        if(user == null) {
            throw new UserNotFoundException("User not found for given id");
        }

        Optional<UserAddress> persistingAddress = user.getAddresses().stream().filter(a -> a.getID() == addressID).findAny();
        if(!persistingAddress.isPresent()) {
            throw new UserAddressNotFoundException("UserAddress not found for given ID");
        }

        user.getAddresses().forEach(address -> address.setDefault(address.getID() == addressID));
        userRepository.saveAndFlush(user);
    }

    public List<UserAddress> getUserAddresses(long userID) {
        User user = userRepository.findOne(userID);
        if(user != null) {
            return user.getAddresses();
        } else {
            return new ArrayList<>();
        }
    }
}
