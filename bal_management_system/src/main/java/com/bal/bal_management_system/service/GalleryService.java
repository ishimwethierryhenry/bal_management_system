package com.bal.bal_management_system.service;

import com.bal.bal_management_system.model.GalleryImage;
import com.bal.bal_management_system.repository.GalleryImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GalleryService {
    private final GalleryImageRepository repository;

    @Value("${upload.path}")
    private String uploadPath;

    public GalleryService(GalleryImageRepository repository) {
        this.repository = repository;
    }

    public GalleryImage saveImage(MultipartFile file, String title, String description) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadPath, fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        GalleryImage image = new GalleryImage();
        image.setTitle(title);
        image.setDescription(description);
        image.setImagePath("/uploads/" + fileName);
        image.setUploadDate(LocalDateTime.now());

        return repository.save(image);
    }

    public List<GalleryImage> getAllImages() {
        return repository.findAll();
    }

    // ... existing code ...

    public void deleteImage(Long id) throws IOException {
        GalleryImage image = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Delete the physical file
        Path imagePath = Paths.get(uploadPath, image.getImagePath().substring("/uploads/".length()));
        Files.deleteIfExists(imagePath);

        // Delete the database record
        repository.deleteById(id);
    }

    public GalleryImage updateImage(Long id, String title, String description, MultipartFile file) throws IOException {
        GalleryImage image = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        image.setTitle(title);
        image.setDescription(description);

        if (file != null && !file.isEmpty()) {
            // Delete old image file if it exists
            if (image.getImagePath() != null) {
                Path oldImagePath = Paths.get(uploadPath, image.getImagePath().substring("/uploads/".length()));
                Files.deleteIfExists(oldImagePath);
            }

            // Save new image file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadPath, fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            image.setImagePath("/uploads/" + fileName);
        }

        return repository.save(image);
    }
}