package com.project.Fabo.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.Fabo.entity.ImageEntity;
import com.project.Fabo.repository.ImageRepository;
import com.project.Fabo.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void storeImage(MultipartFile file, String category) {
        try {
            ImageEntity imageEntity = new ImageEntity(category, file.getBytes());
            imageRepository.save(imageEntity);
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public List<byte[]> getImagesByCategory(String category) {
        // Assuming the repository fetches images as byte arrays from the database
        return imageRepository.findImagesByCategory(category); // Adjust this method based on your repository
    }

    // Add more methods for image retrieval, deletion, etc., as needed
}