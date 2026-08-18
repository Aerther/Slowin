package com.f1project.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.f1project.mapper.CentralMapper;
import com.f1project.model.dto.DriverDTO;
import com.f1project.model.dto.RaceDTO;
import com.f1project.model.dto.TrackDTO;
import com.f1project.model.request.RaceRequest;
import com.f1project.service.DriverService;
import com.f1project.service.RaceService;
import com.f1project.service.TrackService;
import com.f1project.utils.SortList;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/races")
public class RaceController {
	
	private RaceService raceService;
	private DriverService driverService;
	private TrackService trackService;
	private CentralMapper mapper;
	
	// SHOW OPERATION
	
	@GetMapping
	public String showRaces(Model model) {
		List<RaceDTO> races = mapper.races2DTOList(this.raceService.findAllRacesOrderByDateCreatedDesc());
		
		model.addAttribute("races", races);
		model.addAttribute("activePage", "races");
		
		return "races/list";
	}
	
	// CREATE OPERATION
	
	@GetMapping("/create")
	public String showRaceCreationForm(Model model) {
		List<TrackDTO> tracks = mapper.tracks2DTOList(this.trackService.findAllTracksOrderByNameAsc());
		List<DriverDTO> drivers = mapper.drivers2DTOList(this.driverService.findAllDriversOrderByNameAsc());
		
		model.addAttribute("tracks", tracks);
		model.addAttribute("drivers", drivers);
		
		model.addAttribute("activePage", "races");
		
		return "races/create"; 
	}
	
	@PostMapping("/create")
	public String createRace(RaceRequest raceRequest) {
		this.raceService.saveRace(raceRequest);
		
		return "redirect:/races";
	}
	
	// DELETE OPERATION
	
	@GetMapping("/delete")
	public String deleteRace(@RequestParam("raceId") Long raceId) {
		this.raceService.deleteRaceById(raceId);
		
		return "redirect:/races";
	}
}
