package com.f1project.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f1project.service.CountryService;
import com.f1project.service.DriverService;
import com.f1project.service.TeamService;
import com.f1project.service.TrackService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class MainController {
	
	private CountryService countryService;
	private DriverService driverService;
	private TrackService trackService;
	private TeamService teamService;
	
	@GetMapping
	public String mainPageRedirect() {
		return "redirect:/drivers";
	}
	
	@ConditionalOnProperty(name = "app.enable-test-endpoints", havingValue = "true")
	@GetMapping("/create/all")
	public String createAllEntitys() {
		this.countryService.saveCountriesFromClient();
		this.trackService.saveTracksFromTxt();
		this.teamService.createPreMadeTeams();
		this.driverService.createPreMadeDrivers();
		
		return "redirect:/drivers";
	}
}
