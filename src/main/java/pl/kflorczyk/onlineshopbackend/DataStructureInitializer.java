package pl.kflorczyk.onlineshopbackend;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.kflorczyk.onlineshopbackend.model.*;
import pl.kflorczyk.onlineshopbackend.repositories.UserRepository;
import pl.kflorczyk.onlineshopbackend.services.UserService;


@Component
@RequiredArgsConstructor
public class DataStructureInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @NonNull private final UserRepository userRepository;
    @NonNull private final UserService userService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(userRepository.findAll().size() == 0) {
            userService.registerUser("user@gmail.com", "123456");

            User admin = userService.registerUser("admin@gmail.com", "123456");
            admin.setAdmin(true);
            userRepository.saveAndFlush(admin);
        }
    }
}
