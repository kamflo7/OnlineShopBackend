package pl.kflorczyk.onlineshopbackend.rest_controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.services.JwtService;
import pl.kflorczyk.onlineshopbackend.services.UserService;
import pl.kflorczyk.onlineshopbackend.validators.EmailValidator;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserAuth {

    @Autowired private UserService userService;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;


    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test() {// todo:remove
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        node.put("test", "some value");

        return node.toString();
    }

    @PostMapping("/register")
    public String register(@RequestParam(value = "email") String email,
                           @RequestParam(value = "password") String password,
                           HttpServletResponse response) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        try {
            User user = userService.registerUser(email, password);
            response.setHeader("Token", jwtService.tokenFor(user));
        } catch(InvalidEmailException e) {
            return node.put("status", "failure").put("description", "invalid email").toString();
        } catch(EmailAlreadyExistsException e) {
            return node.put("status", "failure").put("description", "email already taken").toString();
        } catch(InvalidPasswordException e) {
            return node.put("status", "failure").put("description", "invalid password").toString();
        }

        return node.put("status", "success").toString();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(value = "email") String email,
                        @RequestParam(value = "password") String password,
                        HttpServletResponse response) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        try {
            User user = userService.loginUser(email, password);
            response.setHeader("Token", jwtService.tokenFor(user));
        } catch (UserNotFoundException e) {
            return node.put("status", "failure").put("description", "user not found").toString();
        } catch (PasswordNotMatchException e) {
            return node.put("status", "failure").put("description", "incorrect password").toString();
        }

        return node.put("status","success").toString();
    }

    @RequestMapping(path = "/authenticatedTestResource", method = RequestMethod.GET)
    public String authTesthResource() {
        return "test";
    }
}
