package com.f1project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f1project.client.ApiClient;
import com.f1project.helper.CentralMapper;
import com.f1project.helper.SortList;
import com.f1project.model.dto.CountryDTO;
import com.f1project.service.CountryService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/countries")
public class CountryController {
	
	private CountryService countryService;
	private CentralMapper mapper;
	
	// SHOW OPERATION
	
	@GetMapping
	public String showCountries(Model model) {
		List<CountryDTO> countriesDTO = SortList.sortCountriesByName(mapper.countries2DTOList(this.countryService.findAllCountries()));
		
		model.addAttribute("countries", countriesDTO);
		model.addAttribute("activePage", "countries");
		
		return "countries/list";
	}
	
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
