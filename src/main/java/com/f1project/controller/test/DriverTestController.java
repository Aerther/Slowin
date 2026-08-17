package com.f1project.controller.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f1project.mapper.CentralMapper;
import com.f1project.service.CountryService;
import com.f1project.service.DriverService;
import com.f1project.service.TeamService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/drivers")
@ConditionalOnProperty(name = "app.enable-test-endpoints", havingValue = "true")
public class DriverTestController {
	private DriverService driverService;
	private CountryService countryService;
	private TeamService teamService;
	private CentralMapper mapper;
	
	@GetMapping("/delete/all")
	public String deleteAllDrivers() {
		this.driverService.deleteAllDrivers();
		
		return "redirect:/drivers";
	}
	
	// CREATE DRIVERS PRE MADE
	@GetMapping("/create/premade")
	public String createPreMadeDrivers() {
		this.driverService.createPreMadeDrivers();
			
		return "redirect:/drivers";
	}	
}
