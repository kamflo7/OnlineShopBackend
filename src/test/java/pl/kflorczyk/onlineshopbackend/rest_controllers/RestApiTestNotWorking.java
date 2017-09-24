package pl.kflorczyk.onlineshopbackend.rest_controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.kflorczyk.onlineshopbackend.config.SecurityConfig;
import pl.kflorczyk.onlineshopbackend.jwt_authentication.JwtAuthFilter;
import pl.kflorczyk.onlineshopbackend.jwt_authentication.JwtAuthenticationEntryPoint;
import pl.kflorczyk.onlineshopbackend.jwt_authentication.JwtAuthenticationProvider;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.rest_controllers.UserAuth;
import pl.kflorczyk.onlineshopbackend.services.JwtService;
import pl.kflorczyk.onlineshopbackend.services.UserService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserAuth.class)
@ContextConfiguration(classes = {SecurityConfig.class})
public class RestApiTestNotWorking {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    JwtService jwtService;

    @MockBean
    PasswordEncoder passwordEncoder;

//    @Autowired
//    ObjectMapper objectMapper;

    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @MockBean
    JwtAuthenticationProvider jwtAuthenticationProvider;

    @MockBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Test
    public void someTest() throws Exception {
        User u = new User();
        u.setEmail("aaa@gmail.com");
        u.setPassword("123456");

        given(userService.registerUser("aaa@gmail.com", "123456")).willReturn(null);

        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .param("email", "aaa@gmail.com")
            .param("password", "123456"))
            .andExpect(status().isOk())
//            .andExpect(model().attribute("status", "success"));
            .andExpect(content().string("{\"status\":\"success\"}"));
    }
}
