package com.parroquia.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parroquia.App.models.service.UserService;

@RestController
public class UserRestController {
	
	@Autowired private UserService service;
	
	@PostMapping("/personal/email")
	public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
		
		return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}

}
