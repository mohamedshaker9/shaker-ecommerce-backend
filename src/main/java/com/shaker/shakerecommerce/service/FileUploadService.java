package com.shaker.shakerecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {


    public String uploadProductImage(String path, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String randomFileName = UUID.randomUUID() + extension;

        String newFilePath = path.toString() + File.separator + randomFileName;

        File dest = new File(newFilePath);
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(newFilePath));
        return randomFileName;

    }
}
