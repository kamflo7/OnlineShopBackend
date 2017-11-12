package pl.kflorczyk.onlineshopbackend.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.kflorczyk.onlineshopbackend.dto.CompanyAddressDTO;
import pl.kflorczyk.onlineshopbackend.dto.PersonAddressDTO;
import pl.kflorczyk.onlineshopbackend.dto.UserAddressDTO;
import pl.kflorczyk.onlineshopbackend.exceptions.*;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.model.UserAddress;
import pl.kflorczyk.onlineshopbackend.rest_controllers.responses.Response;
import pl.kflorczyk.onlineshopbackend.rest_controllers.responses.ResponseDetail;
import pl.kflorczyk.onlineshopbackend.services.JwtService;
import pl.kflorczyk.onlineshopbackend.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserController {

    @Autowired private UserService userService;
    @Autowired private JwtService jwtService;

    @PostMapping("/register")
    public Response register(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            HttpServletResponse response) {
        try {
            User user = userService.registerUser(email, password);
            response.setHeader("Token", jwtService.tokenFor(user));
        } catch(InvalidEmailException |
                EmailAlreadyExistsException |
                InvalidPasswordException e) {
            return new Response(ResponseDetail.Status.FAILURE, e.getMessage());
        }

        return new Response(ResponseDetail.Status.SUCCESS);
    }

    @PostMapping(path = "/login")
    public Response login(@RequestParam(value = "email") String email,
                                      @RequestParam(value = "password") String password,
                                      HttpServletResponse response) {
        try {
            User user = userService.loginUser(email, password);
            response.setHeader("Token", jwtService.tokenFor(user));
        } catch (UserNotFoundException |
                PasswordNotMatchException e) {
            return new Response(Response.Status.FAILURE, e.getMessage());
        }

        return new Response(Response.Status.SUCCESS);
    }

    @PutMapping(path = "/users/{userID}/addresses/person")
    public Response createAddressPerson(@PathVariable(name = "userID") long userID, @RequestBody PersonAddressDTO dto) {

        return createAddress(userID, dto);
    }

    @PutMapping(path = "/users/{userID}/addresses/company")
    public Response createAddressCompany(@PathVariable(name = "userID") long userID, @RequestBody CompanyAddressDTO dto) {
        return createAddress(userID, dto);
    }

    @PostMapping(path = "/users/{userID}/addresses/{addressID}/person")
    public Response editAddress(@PathVariable(name = "userID") long userID, @PathVariable(name = "addressID") long addressID, @RequestBody PersonAddressDTO dto) {
        return editAddress(userID, addressID, dto);
    }

    @PostMapping(path = "/users/{userID}/addresses/{addressID}/company")
    public Response editAddress(@PathVariable(name = "userID") long userID, @PathVariable(name = "addressID") long addressID, @RequestBody CompanyAddressDTO dto) {
        return editAddress(userID, addressID, dto);
    }

    @PutMapping(path = "/users/{userID}/addresses/{addressID}/default")
    public Response setDefaultAddress(@PathVariable(name = "userID") long userID, @PathVariable(name = "addressID") long addressID) {
        try {
            userService.setDefaultAddress(userID, addressID);
        } catch(UserNotFoundException | UserAddressNotFoundException e) {
            return new Response(Response.Status.FAILURE, e.getMessage());
        }
        return new Response(Response.Status.SUCCESS);
    }

    @GetMapping(path = "/users/{userID}/addresses")
    public Response getAddresses(@PathVariable(name = "userID") long id) {
        return new ResponseDetail<>(userService.getUserAddresses(id));
    }

    private Response createAddress(long userID, UserAddressDTO dto) {
        try {
            userService.createUserAddress(userID, dto);
        } catch(NullPointerException | InvalidAddressException | UserNotFoundException e) {
            return new Response(Response.Status.FAILURE, e.getMessage());
        }
        return new Response(Response.Status.SUCCESS);
    }

    private Response editAddress(long userID, long addressID, UserAddressDTO dto) {
        try {
            userService.editUserAddress(userID, addressID, dto);
        } catch(NullPointerException | InvalidAddressException | UserNotFoundException | UserAddressNotFoundException e) {
            return new Response(Response.Status.FAILURE, e.getMessage());
        }
        return new Response(Response.Status.SUCCESS);
    }

}
