package com.project.Fabo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Fabo.entity.Creatives;
import com.project.Fabo.repository.CreativesRepository;
import com.project.Fabo.service.CreativesService;

@Service
public class CreativesServiceImpl implements CreativesService{
	
	@Autowired
	private CreativesRepository creativesRepository;

	@Override
	public void save(Creatives creatives) {
		creativesRepository.save(creatives);
		
	}

	@Override
	public List<Creatives> findByServiceType(String string) {
	    return creativesRepository.findByServiceType(string);
	}

	@Override
	public byte[] getBaseImageById(Long creativeId) {
	    // Retrieve the Creative entity from the database using the provided creativeId
	    Optional<Creatives> optionalCreative = creativesRepository.findById(creativeId);

	    // Check if the Creative entity exists
	    if (optionalCreative.isPresent()) {
	        Creatives creative = optionalCreative.get();

	        // Assuming the base image data is stored as a byte array in the Creative entity
	        byte[] baseImageData = creative.getBaseImage();

	        // Return the base image data
	        return baseImageData;
	    } else {
	        // If the Creative entity does not exist for the provided creativeId, return null or throw an exception
	        return null; // or throw new ImageNotFoundException("Base image data not found for creativeId: " + creativeId);
	    }
	}



}
