package pl.kflorczyk.onlineshopbackend.rest_controllers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import pl.kflorczyk.onlineshopbackend.config.SecurityConfig;
import pl.kflorczyk.onlineshopbackend.exceptions.InvalidPasswordException;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.rest_controllers.UserAuth;
import pl.kflorczyk.onlineshopbackend.services.UserService;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnSuccessAndTokenHeader() throws Exception {
        String email = "johndoe@gmail.com";
        String pass = "1234567";

        User userToReturn = new User();
        userToReturn.setEmail(email);
        userToReturn.setPassword(pass);

        given(userService.registerUser(email, pass)).willReturn(userToReturn);

        this.mockMvc.perform(post("/register")
            .param("email", email)
            .param("password", pass)
        ).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("success")))
         .andExpect(header().string("Token", isNotEmpty));
    }

    @Test
    public void shouldReturnThatPasswordIsInvalid() throws Exception {
        String email = "johndoe@gmail.com";
        String pass = "1234";

        User userToReturn = new User();
        userToReturn.setEmail(email);
        userToReturn.setPassword(pass);

        given(userService.registerUser(email, pass)).willThrow(InvalidPasswordException.class);

        this.mockMvc.perform(post("/register")
                .param("email", email)
                .param("password", pass)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("invalid password")));
    }

    Matcher<String> isNotEmpty = new Matcher<String>() {
        @Override
        public boolean matches(Object item) {
            if(item == null)
                return false;

            String s = (String) item;
            return s.length() > 0;
        }

        @Override
        public void describeMismatch(Object item, Description mismatchDescription) {

        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

        }

        @Override
        public void describeTo(Description description) {

        }
    };
}
