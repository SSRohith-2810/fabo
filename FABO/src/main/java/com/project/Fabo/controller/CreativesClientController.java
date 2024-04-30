package com.project.Fabo.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.Fabo.entity.CreativeWithImage;
import com.project.Fabo.entity.Creatives;
import com.project.Fabo.repository.CreativesRepository;
import com.project.Fabo.service.CreativesService;

@Controller
public class CreativesClientController {
	
	@Autowired
	private CreativesService creativesService;
	
	@Autowired
	private CreativesRepository creativesRepository;
	
	@GetMapping("/viewClient")
	public String viewClient() {
		return "clientcreatives";
	}
	
	@GetMapping("/laundryClient")
	public String laundryClient(Model model) {
		List<Creatives> laundryCreatives = creativesService.findByServiceType("Dry Cleaning");
	       
	       // Convert images to base64 strings along with their IDs
	       List<CreativeWithImage> creativeWithImages = new ArrayList<>();
	       for (Creatives creative : laundryCreatives) {
	           if (creative.getCanvasImage() != null) {
	               CreativeWithImage cwi = new CreativeWithImage();
	               cwi.setId(creative.getCreativeId());
	               cwi.setBase64Image(Base64.getEncoder().encodeToString(creative.getCanvasImage()));
	               creativeWithImages.add(cwi);
	           }
	       }
	       
	       model.addAttribute("creativeWithImages", creativeWithImages);
		return "clientlaundry";
	}
	
}