package com.project.Fabo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.Fabo.service.ClientService;
import com.project.Fabo.service.UserService;

@RestController
public class ErrorController {
	
	 @Autowired
	 private UserService userService;
	 
	 @Autowired
	 private ClientService clientService;
	
	 @GetMapping("/checkUsernameAvailability")
	 @ResponseBody
	 public Map<String, Boolean> checkUsernameAvailability(@RequestParam String username) {
	     boolean isAvailable = !userService.isUsernameDuplicate(username);
	     Map<String, Boolean> response = new HashMap<>();
	     response.put("available", isAvailable);
	     return response;
	 }

	 @GetMapping("/checkStoreCodeAvailability")
	 @ResponseBody
	 public Map<String, Boolean> checkStoreCodeAvailability(@RequestParam String storeCode) {
	     boolean isAvailable = !clientService.isStoreCodeDuplicate(storeCode);
	     Map<String, Boolean> response = new HashMap<>();
	     response.put("available", isAvailable);
	     return response;
	 }
}
