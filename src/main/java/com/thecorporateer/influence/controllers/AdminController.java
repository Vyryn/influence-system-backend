package com.thecorporateer.influence.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thecorporateer.influence.services.CorporateerHandlingService;

@RestController
public class AdminController {
	
	@Autowired
	CorporateerHandlingService corporateerHandlingService;
	
	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/distributeTributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> distributeTributes() {
		corporateerHandlingService.distributeTributes();
		return ResponseEntity.ok().body("{\"message\":\"Distribution successful\"}");
	}
}