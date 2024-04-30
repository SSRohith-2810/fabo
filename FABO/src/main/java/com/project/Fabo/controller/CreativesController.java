package com.project.Fabo.controller;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.Fabo.repository.ImageRepository;
import com.project.Fabo.service.CreativesService;
import com.project.Fabo.entity.CreativeWithImage;
import com.project.Fabo.entity.Creatives;

@Controller
public class CreativesController {
	
	@Autowired
	private CreativesService creativesService;
	
   @GetMapping("/show")
   public String show() {
	   return "add_creative";
   }
   
   @PostMapping("/showw")
   public String Creatives(@RequestParam("creativeName") String creativeName,
           @RequestParam("templateStyle") String templateStyle,
           @RequestParam("templateType") String templateType,
           @RequestParam("serviceType") String serviceType,
           @RequestParam("discount") String discount,
           @RequestParam("phoneNumber") String phoneNumber,
           @RequestParam("address") String address,
           @RequestParam("textStyle1") String textStyle1,
           @RequestParam("textStyle2") String textStyle2,
           @RequestParam("textStyle3") String textStyle3,
           @RequestParam("color1") String color1,
           @RequestParam("color2") String color2,
           @RequestParam("color3") String color3,
           @RequestParam("imageInput") MultipartFile uploadedfileData,
           @RequestParam("canvasImageData") String canvasImageData, // Change type to String
           Model model) {

       // Create Creatives object
       Creatives creatives = new Creatives();
       model.addAttribute("creatives", creatives);

       // Set other fields
       creatives.setCreativeName(creativeName);
       creatives.setTemplateStyle(templateStyle);
       creatives.setTemplateType(templateType);
       creatives.setServiceType(serviceType);
       creatives.setDiscount(discount);
       creatives.setPhoneNumber(phoneNumber);
       creatives.setAddress(address);
       creatives.setTextStyle1(textStyle1);
       creatives.setTextStyle2(textStyle2);
       creatives.setTextStyle3(textStyle3);
       creatives.setColor1(color1);
       creatives.setColor2(color2);
       creatives.setColor3(color3);
       // Set other fields

       // Save the files
       try {
           if (!uploadedfileData.isEmpty()) {
               byte[] uploadedFileBytes = uploadedfileData.getBytes();
               // Save uploadedFileBytes to database
               creatives.setBaseImage(uploadedFileBytes);
           }
           
           // Save canvasImageBytes to database
           String[] parts = canvasImageData.split(",");
           byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
           creatives.setCanvasImage(decodedBytes);
       } catch (IOException e) {
           e.printStackTrace(); // Handle the exception properly
       }

       // Save Creatives object to the database
       
       creativesService.save(creatives);
       System.out.println(creativeName);

       return "add_creative";
   }
   
  
}