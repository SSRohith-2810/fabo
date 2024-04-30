package com.project.Fabo.controller;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.Fabo.service.ImageService;

@Controller
public class ImageRetrieveController {

    @Autowired
    private ImageService imageService;
/*
    @GetMapping("/category/{category}")
    public String retrieveImagesByCategory(@PathVariable String category, Model model) {
    	 List<byte[]> images = imageService.getImagesByCategory(category);
        model.addAttribute("category", category);
        model.addAttribute("images", images);
        return "creativesDownload"; // Render the view with images based on category
    }
*/
    
    public ImageRetrieveController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/category/{category}")
    public String retrieveImagesByCategory(@PathVariable String category, Model model) {
        List<byte[]> images = imageService.getImagesByCategory(category);
        model.addAttribute("category", category);
        model.addAttribute("imagesAsBase64", convertImagesToBase64(images));
        return "creativesDownload"; // Render the view with images based on category
    }

    private List<String> convertImagesToBase64(List<byte[]> images) {
        // Convert byte arrays to Base64 strings
        return images.stream()
                .map(image -> Base64.getEncoder().encodeToString(image))
                .collect(Collectors.toList());
    }
    
    @GetMapping("/dryclean")
    public String showDryClean() {
        return "creativesDownload "; // Assuming you have a "upload.html" view
    }
    
    @GetMapping("/laundry")
    public String showLaundry() {
        return "creativesDownload"; // Assuming you have a "upload.html" view
    }
    
    @GetMapping("/carpetsclean")
    public String showCarpetsclean() {
        return "creativesDownload"; // Assuming you have a "upload.html" view
    }
    
    @GetMapping("/bagsclean")
    public String showBagsclean() {
        return "creativesDownload"; // Assuming you have a "upload.html" view
    }
    
    @GetMapping("/leather")
    public String showLeather() {
        return "creativesDownload"; // Assuming you have a "upload.html" view
    }
    // Other endpoints for different retrieval functionalities if needed
}