package com.f1project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.f1project.model.dto.DriverDTO;
import com.f1project.model.dto.RaceDTO;
import com.f1project.model.dto.TrackDTO;
import com.f1project.request.RaceRequest;
import com.f1project.service.DriverService;
import com.f1project.service.RaceService;
import com.f1project.service.TrackService;
import com.f1project.utils.SortList;
import com.f1project.utils.mapper.CentralMapper;

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
		List<RaceDTO> racesDTO = SortList.sortRacesByName(mapper.races2DTOList(this.raceService.findAllRaces()));
		
		model.addAttribute("races", racesDTO);
		model.addAttribute("activePage", "races");
		
		return "races/list";
	}
	
	// CREATE OPERATION
	
	@GetMapping("/create")
	public String showRaceCreationForm(Model model) {
		List<TrackDTO> tracksDTO = SortList.sortTracksByName(mapper.tracks2DTOList(this.trackService.findAllTracks()));
		List<DriverDTO> driversDTO = SortList.sortDriversByName(mapper.drivers2DTOList(this.driverService.findAllDrivers()));
		
		model.addAttribute("tracks", tracksDTO);
		model.addAttribute("drivers", driversDTO);
		
		return "races/create"; 
	}
	
	@PostMapping("/create")
	public String createRace(RaceRequest raceRequest) {
		this.raceService.saveRace(raceRequest);
		
		return "redirect:/races";
	}
	
	// UPDATE OPERATION
	
	@GetMapping("/update/{raceId}")
	public String showRaceEditForm(@PathVariable("raceId") Long raceId, Model model) {
		List<TrackDTO> tracksDTO = mapper.tracks2DTOList(this.trackService.findAllTracks());
		List<DriverDTO> driversDTO = mapper.drivers2DTOList(this.driverService.findAllDrivers());
		RaceDTO raceDTO = mapper.race2DTO(this.raceService.findRaceById(raceId));
		
		model.addAttribute("race", raceDTO);
		model.addAttribute("tracks", tracksDTO);
		model.addAttribute("drivers", driversDTO);
		
		return "races/update";
	}
	
	@PostMapping("/update")
	public String editRace(RaceRequest raceRequest) {
		this.raceService.updateRace(raceRequest);
		
		return "redirect:/races";
	}
	
	// DELETE OPERATION
	
	@GetMapping("/delete")
	public String deleteRace(@RequestParam("raceId") Long raceId) {
		this.raceService.deleteRaceById(raceId);
		
		return "redirect:/races";
	}
	
	@GetMapping("/delete/all")
	public String deleteAllRaces() {
		this.raceService.deleteAllRaces();
		
		return "redirect:/races";
	}
}
