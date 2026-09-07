package com.faiyaz.SeekersStop.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadPath = Paths.get("uploads/cv");

    public String storeCv(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalStateException("CV file is required");
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new IllegalStateException("Only PDF files are allowed");
        }

        try {
            Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + ".pdf";

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            return filePath.toString();

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to store CV");
        }
    }
}