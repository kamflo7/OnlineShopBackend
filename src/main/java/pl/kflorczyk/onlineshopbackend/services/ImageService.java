package pl.kflorczyk.onlineshopbackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class ImageService {
    public static final String IMGS_PATH = "imgs/";

    @Value("${resourceDirectory}")
    private String resourceDirectory;

    public ImageService() {
        File absolute = new File(resourceDirectory + IMGS_PATH);
        if(!absolute.exists()) {
            absolute.mkdirs();
        }
    }

    public void saveImageBase64OnDisk(String base64, String imageName) {
        File absolute = new File(resourceDirectory + IMGS_PATH);
        if(!absolute.exists()) {
            absolute.mkdirs();
        }

        byte[] decoded = Base64.getDecoder().decode(base64);

        try {
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(decoded));
            File destination = new File(absolute.getAbsolutePath()+"/"+imageName);
            ImageIO.write(src, "png", destination);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public byte[] loadImageFromDisk(String imageName) {
        File absolute = new File(resourceDirectory + IMGS_PATH);
        Path path = Paths.get(absolute.toString()+"/"+imageName);

        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bytes;
    }

    public void replaceImageBase64OnDisk(String base64, String imageName) {
        File absolute = new File(resourceDirectory + IMGS_PATH);
        File current = new File(absolute.getAbsolutePath()+"/"+imageName);
        current.delete();

        byte[] decoded = Base64.getDecoder().decode(base64);

        try {
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(decoded));
            File destination = new File(absolute.getAbsolutePath()+"/"+imageName);
            ImageIO.write(src, "png", destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
