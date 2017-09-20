package pl.kflorczyk.onlineshopbackend.rest_controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.services.JwtService;
import pl.kflorczyk.onlineshopbackend.services.UserService;
import pl.kflorczyk.onlineshopbackend.validators.EmailValidator;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserAuthentication {

    @Autowired private UserService userService;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;


    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test() {// todo:remove
        ObjectNode node = JsonNodeFactory.instance.objectNode();


        return node.toString();
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(@RequestParam(value = "email") String email,
                           @RequestParam(value = "password") String password,
                           HttpServletResponse response) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        if(!EmailValidator.validate(email))
            return node.put("status", "failure")
                        .put("description", "email invalid").toString();

        if(!userService.isUserExists(email)) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));

            userService.createUser(user);
            response.setHeader("Token", jwtService.tokenFor(user));

            System.out.println(String.format("UserService::createUser -> [%d '%s' rawPass: '%s'; encoded: '%s']",
                    user.getID(), user.getEmail(), password, user.getPassword()));

            return node.put("status", "success").toString();
        } else {
            return node.put("status", "failure")
                        .put("description", "user already exists").toString();
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(value = "email") String email,
                        @RequestParam(value = "password") String password,
                        HttpServletResponse response) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        System.out.println(String.format("UserAuthentication::login '%s' with raw '%s' -> '%s'",
                email, password, passwordEncoder.encode(password)));

        User user = userService.getUser(email);

        if(user == null)
            return node.put("status","failure")
                    .put("description", "user not found").toString();


        if(!passwordEncoder.matches(password, user.getPassword()))
            return node.put("status","failure")
                    .put("description", "incorrect password").toString();

        response.setHeader("Token", jwtService.tokenFor(user));

        return node.put("status","success").toString();
    }

    @RequestMapping(path = "/authenticatedTestResource", method = RequestMethod.GET)
    public String authTesthResource() {
        return "test";
    }
}
