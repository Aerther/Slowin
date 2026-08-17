package com.f1project.controller.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f1project.mapper.CentralMapper;
import com.f1project.service.CountryService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/countries")
@ConditionalOnProperty(name = "app.enable-test-endpoints", havingValue = "true")
public class CountryTestController {
	private CountryService countryService;
	private CentralMapper mapper;
	
	// CREATE OPERATION
	
	@GetMapping("/create")
	public String createCountries() {
		this.countryService.saveCountriesFromClient();
			
		return "redirect:/countries";
	}
		
	// DELETE OPERATION
		
	@GetMapping("/delete/all")
	public String deleteAllCountries() {
		this.countryService.deleteAllCountries();
			
		return "redirect:/countries";
	}
}
