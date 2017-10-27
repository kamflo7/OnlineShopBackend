package pl.kflorczyk.onlineshopbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.kflorczyk.onlineshopbackend.jwt_authentication.JwtAuthFilter;
import pl.kflorczyk.onlineshopbackend.jwt_authentication.JwtAuthenticationEntryPoint;
import pl.kflorczyk.onlineshopbackend.jwt_authentication.JwtAuthenticationProvider;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthEndPoint;

    public SecurityConfig() {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
                .and()
            .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/test").permitAll() // todo:remove
                .antMatchers("/categories").permitAll()
                .antMatchers("/categories/**").permitAll()
                .antMatchers("/products").permitAll()
                .antMatchers("/products/**").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/resources/imgs/**").permitAll()
                .antMatchers("/navigations").permitAll()
                .antMatchers("/navigations/**").permitAll()
                .antMatchers("/authenticatedTestResource").hasAuthority("ROLE_USER")
//                .antMatchers("/user/*", "/user", "/userlist").permitAll()
//                .antMatchers("/admin").hasAuthority("ROLE_USER")
                .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEndPoint)
                .and()
            .csrf().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "X-Auth-Token", "Authorization"));
        configuration.setExposedHeaders(Arrays.asList("Origin", "Accept", "Content-Type", "Location", "Token"));
        //configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}
