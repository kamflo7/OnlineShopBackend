package pl.kflorczyk.onlineshopbackend.rest_controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.rest_controllers.responses.Response;
import pl.kflorczyk.onlineshopbackend.services.JwtService;
import pl.kflorczyk.onlineshopbackend.services.UserService;
import pl.kflorczyk.onlineshopbackend.validators.EmailValidator;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired private UserService userService;
    @Autowired private JwtService jwtService;

    @PostMapping("/register")
    public Response<User> register(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            HttpServletResponse response) {
        try {
            User user = userService.registerUser(email, password);
            response.setHeader("Token", jwtService.tokenFor(user));
        } catch(InvalidEmailException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        } catch(EmailAlreadyExistsException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        } catch(InvalidPasswordException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }

        return new Response<>(Response.Status.SUCCESS);
    }

    @PostMapping(path = "/login")
    public Response<User> login(@RequestParam(value = "email") String email,
                        @RequestParam(value = "password") String password,
                        HttpServletResponse response) {
        try {
            User user = userService.loginUser(email, password);
            response.setHeader("Token", jwtService.tokenFor(user));
        } catch (UserNotFoundException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        } catch (PasswordNotMatchException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }

        return new Response<>(Response.Status.SUCCESS);
    }
}
