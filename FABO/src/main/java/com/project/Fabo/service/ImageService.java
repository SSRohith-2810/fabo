package com.project.Fabo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public interface ImageService {
    void storeImage(MultipartFile file, String category);
    List<byte[]> getImagesByCategory(String category); 
    // Add more methods as needed
}