package com.f1project.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f1project.client.ApiClient;
import com.f1project.mapper.CentralMapper;
import com.f1project.model.dto.CountryDTO;
import com.f1project.service.CountryService;
import com.f1project.utils.SortList;

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
		List<CountryDTO> countries = mapper.countries2DTOList(this.countryService.findAllCountriesOrderByBrazilianAsc());
		
		model.addAttribute("countries", countries);
		model.addAttribute("activePage", "countries");
		
		return "countries/list";
	}
}
