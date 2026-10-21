package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.UserDefinedExceptions.IllegalStateException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadPath;
    private final Path applicationCvPath;

    public FileStorageService() {
        this("uploads");
    }

    @Autowired
    public FileStorageService(@Value("${app.storage.root:uploads}") String storageRoot) {
        Path root = Paths.get(storageRoot).toAbsolutePath().normalize();
        this.uploadPath = root.resolve("cv");
        this.applicationCvPath = root.resolve("application-cv");
    }

    public String storeCv(MultipartFile file) {

        validateCv(file);

        try {
            Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + ".pdf";

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            return filePath.toString();

        } catch (IOException e) {
            throw new IllegalStateException("Failed to store CV");
        }
    }

    public void deleteCv(String cvPath) {

        if (cvPath == null || cvPath.isBlank()) {
            return;
        }

        try {
            Path path = Paths.get(cvPath);

            Files.deleteIfExists(path);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to delete old CV");
        }
    }

    public Resource loadCv(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) {
            throw new ResourceNotFoundException("Resume not found");
        }

        Path uploadRoot = uploadPath.toAbsolutePath().normalize();
        Path cvPath;
        try {
            cvPath = Paths.get(storedPath).toAbsolutePath().normalize();
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Resume not found");
        }

        if (!cvPath.startsWith(uploadRoot) || !Files.isRegularFile(cvPath)) {
            throw new ResourceNotFoundException("Resume not found");
        }

        try {
            return new UrlResource(cvPath.toUri());
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Resume not found");
        }
    }

    public void ensureCurrentCvExists(String storedPath) {
        Path cvPath = resolveStoredPath(storedPath, uploadPath);
        if (!Files.isRegularFile(cvPath)) {
            throw new ResourceNotFoundException("CV file not found");
        }
    }

    public String copyCvForApplication(String currentCvPath) {
        ensureCurrentCvExists(currentCvPath);
        Path sourcePath = resolveStoredPath(currentCvPath, uploadPath);
        Path snapshotPath = applicationCvPath.resolve(UUID.randomUUID() + ".pdf");
        try {
            Files.createDirectories(applicationCvPath);
            Files.copy(sourcePath, snapshotPath);
            return snapshotPath.toString();
        } catch (IOException e) {
            try {
                Files.deleteIfExists(snapshotPath);
            } catch (IOException cleanupFailure) {
                e.addSuppressed(cleanupFailure);
            }
            throw new IllegalStateException("Failed to preserve the application resume");
        }
    }

    public Resource loadApplicationCv(String storedPath) {
        Path cvPath = resolveStoredPath(storedPath, applicationCvPath);
        if (!Files.isRegularFile(cvPath)) {
            throw new ResourceNotFoundException("Resume not found");
        }
        try {
            return new UrlResource(cvPath.toUri());
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Resume not found");
        }
    }

    public void deleteApplicationCv(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) return;
        Path cvPath = resolveStoredPath(storedPath, applicationCvPath);
        try {
            Files.deleteIfExists(cvPath);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to clean up the application resume");
        }
    }

    private Path resolveStoredPath(String storedPath, Path allowedRoot) {
        if (storedPath == null || storedPath.isBlank()) {
            throw new ResourceNotFoundException("Resume not found");
        }

        Path root = allowedRoot.toAbsolutePath().normalize();
        Path resolvedPath;
        try {
            resolvedPath = Paths.get(storedPath).toAbsolutePath().normalize();
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Resume not found");
        }

        Path fileName = resolvedPath.getFileName();
        if (!resolvedPath.startsWith(root) || fileName == null || !fileName.toString().toLowerCase().endsWith(".pdf")) {
            throw new ResourceNotFoundException("Resume not found");
        }
        return resolvedPath;
    }

    private void validateCv(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalStateException("CV file is required");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null ||
                !fileName.toLowerCase().endsWith(".pdf")) {

            throw new IllegalStateException(
                    "Only PDF files are allowed"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !contentType.equalsIgnoreCase("application/pdf")) {

            throw new IllegalStateException(
                    "Only PDF files are allowed"
            );
        }

        long maxSize = 5 * 1024 * 1024;

        if (file.getSize() > maxSize) {

            throw new IllegalStateException(
                    "CV size must not exceed 5 MB"
            );
        }
    }
}
