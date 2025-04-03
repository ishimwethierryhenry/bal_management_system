package com.bal.bal_management_system.controller;

import com.bal.bal_management_system.model.GalleryImage;
import com.bal.bal_management_system.service.GalleryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class GalleryController {
    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    // Public gallery access for all users
    @GetMapping("/gallery")
    public String showGallery(Model model, HttpSession session) {
        model.addAttribute("images", galleryService.getAllImages());
        model.addAttribute("isAdmin", "ADMIN".equalsIgnoreCase((String) session.getAttribute("role")));
        return "gallery";
    }

    // Admin-specific gallery management
    @GetMapping("/admin/gallery")
    public String showAdminGallery(Model model, HttpSession session) {
        model.addAttribute("images", galleryService.getAllImages());
        model.addAttribute("isAdmin", true);
        return "gallery";
    }

    @GetMapping("/admin/gallery/upload")
    public String showUploadForm() {
        return "upload-image";
    }

    @PostMapping("/admin/gallery/upload")
    public String handleImageUpload(@RequestParam("file") MultipartFile file,
                                    @RequestParam("title") String title,
                                    @RequestParam("description") String description) {
        try {
            galleryService.saveImage(file, title, description);
            return "redirect:/admin/gallery";
        } catch (Exception e) {
            return "redirect:/admin/gallery/upload?error";
        }
    }

    // ... existing code ...

    @PostMapping("/admin/gallery/delete/{id}")
    public String deleteImage(@PathVariable Long id) {
        try {
            galleryService.deleteImage(id);
            return "redirect:/admin/gallery";
        } catch (Exception e) {
            return "redirect:/admin/gallery?error";
        }
    }

    @GetMapping("/admin/gallery/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        GalleryImage image = galleryService.getAllImages().stream()
                .filter(img -> img.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Image not found"));

        model.addAttribute("image", image);
        return "edit-image";
    }

    @PostMapping("/admin/gallery/edit/{id}")
    public String handleImageUpdate(@PathVariable Long id,
                                    @RequestParam("title") String title,
                                    @RequestParam("description") String description,
                                    @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            galleryService.updateImage(id, title, description, file);
            return "redirect:/admin/gallery";
        } catch (Exception e) {
            return "redirect:/admin/gallery/edit/" + id + "?error";
        }
    }
}