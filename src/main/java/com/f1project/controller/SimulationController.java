package com.f1project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.f1project.helper.CentralMapper;
import com.f1project.model.dto.RaceDTO;
import com.f1project.service.CountryService;
import com.f1project.service.RaceService;
import com.f1project.service.SimulationService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/simulation")
public class SimulationController {
	
	private SimulationService simulationService;
	private RaceService raceService;
	private CentralMapper mapper;
	
	@GetMapping("{raceId}")
	public String showSimulationRace(@PathVariable("raceId") Long raceId, Model model) {
		this.simulationService.simulateRace(raceId, 0);
		
		RaceDTO raceDTO = mapper.race2DTO(this.raceService.findRaceById(raceId));
		
		model.addAttribute("race", raceDTO);
		
		return "races/simulate";
	}
	
	@GetMapping("{raceId}/simulate")
	public String simulateRace(@PathVariable("raceId") Long raceId, @RequestParam("laps") int laps, Model model) {
		simulationService.simulateRace(raceId, laps);
		
		return "redirect:/simulation/" + raceId;
	}
	
	@GetMapping("{raceId}/finish")
	public String finishRace(@PathVariable("raceId") Long raceId, Model model) {
		this.simulationService.finishRace(raceId);
		
		return "redirect:/simulation/" + raceId;
	}
}
