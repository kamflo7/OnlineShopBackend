package pl.kflorczyk.onlineshopbackend.rest_controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.kflorczyk.onlineshopbackend.model.Image;
import pl.kflorczyk.onlineshopbackend.repositories.ImageRepository;

@RestController
@RequiredArgsConstructor
public class ResourceController {

    @NonNull private final ImageRepository imageRepository;

    @GetMapping(path = "/resources/imgs/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") long id) {
        Image image = imageRepository.findOne(id);
        if(image != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
