package coverit.image_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ImageServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ImageServerApp.class, args);
    }
}