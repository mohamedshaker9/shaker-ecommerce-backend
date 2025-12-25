package com.shaker.shakerecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Resolve absolute path to product_images folder
        Path uploadDir = Paths.get("product_images");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        System.out.println("Serving product images from: " + uploadPath);

        registry.addResourceHandler("/product_images/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
