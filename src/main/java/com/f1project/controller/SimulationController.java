package com.f1project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f1project.helper.CentralMapper;
import com.f1project.service.CountryService;
import com.f1project.service.SimulationService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/simulate")
public class SimulationController {
	
	private SimulationService simulationService;
	
	@GetMapping("{raceId}")
	public String simulateRace(@PathVariable("raceId") Long raceId) {
		int laps = 1;
		
		simulationService.simulateRace(raceId, laps);
		
		return "redirect:/races";
	}
}
