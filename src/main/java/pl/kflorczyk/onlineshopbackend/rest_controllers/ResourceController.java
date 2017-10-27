package pl.kflorczyk.onlineshopbackend.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.kflorczyk.onlineshopbackend.services.ImageService;

@RestController
public class ResourceController {

    @Autowired
    private ImageService imageService;

    @GetMapping(path = "/resources/imgs/{name:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String image) {
        byte[] bytes = imageService.loadImageFromDisk(image);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }
}
