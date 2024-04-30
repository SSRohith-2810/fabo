package com.project.Fabo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.Fabo.service.ImageService;

@Controller
public class ImageUploadController { 

    @Autowired
    private ImageService imageService;
    
    @GetMapping("/creatives")
    public String showCreatives() {
    	return "creatives";
    }

    @PostMapping("/upload/store")
    public String uploadMultipleImages(
            @RequestParam("file1") MultipartFile file1, 
            @RequestParam("file2") MultipartFile file2,
            @RequestParam("file3") MultipartFile file3,
            @RequestParam("file4") MultipartFile file4,
            @RequestParam("file5") MultipartFile file5,
            @RequestParam("file6") MultipartFile file6,
            @RequestParam("file7") MultipartFile file7,
            @RequestParam("file8") MultipartFile file8,
            @RequestParam("category") String category) {
        
        if (!file1.isEmpty()) {
            imageService.storeImage(file1, category);
        }
        if (!file2.isEmpty()) {
            imageService.storeImage(file2, category);
        }
        if (!file3.isEmpty()) {
            imageService.storeImage(file3, category);
        }
        if (!file4.isEmpty()) {
            imageService.storeImage(file4, category);
        }
        if (!file5.isEmpty()) {
            imageService.storeImage(file5, category);
        }
        if (!file6.isEmpty()) {
            imageService.storeImage(file6, category);
        }
        if (!file7.isEmpty()) {
            imageService.storeImage(file7, category);
        }
        if (!file8.isEmpty()) {
            imageService.storeImage(file8, category);
        }
        
        return "redirect:/creatives"; // Redirect to a specific page after uploading
    }


    // Other endpoints for different categories if needed
}