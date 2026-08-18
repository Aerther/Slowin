package com.f1project.controller.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f1project.service.CountryService;
import com.f1project.service.DriverService;
import com.f1project.service.RaceEventService;
import com.f1project.service.RaceResultService;
import com.f1project.service.RaceService;
import com.f1project.service.TeamService;
import com.f1project.service.TrackService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/")
@ConditionalOnProperty(name = "app.enable-test-endpoints", havingValue = "true")
public class MainTestController {
	private CountryService countryService;
	private DriverService driverService;
	private TrackService trackService;
	private RaceService raceService;
	private TeamService teamService;
	private RaceEventService raceEventService;
	
	@GetMapping("/delete/all")
	public String deleteAllEntities() {
		this.raceEventService.deleteAllRaceEvents();
		this.raceService.deleteAllRaces();
		this.driverService.deleteAllDrivers();
		this.trackService.deleteAllTracks();
		this.teamService.deleteAllTeams();
		this.countryService.deleteAllCountries();
		
		return "redirect:/drivers";
	}
	
	@GetMapping("/create/all")
	public String createAllEntities() {
		this.countryService.saveCountriesFromClient();
		this.trackService.saveTracksFromTxt();
		this.teamService.createPreMadeTeams();
		this.driverService.createPreMadeDrivers();
		
		return "redirect:/drivers";
	}
	
	@GetMapping("/create/class")
	public String createClassRace() {
		this.raceEventService.deleteAllRaceEvents();
		this.raceService.deleteAllRaces();
		this.driverService.deleteAllDrivers();
		this.driverService.createClassDrivers();
		
		return "redirect:/drivers";
	}
}
