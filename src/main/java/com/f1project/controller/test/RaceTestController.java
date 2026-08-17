package com.f1project.controller.test;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f1project.mapper.CentralMapper;
import com.f1project.model.dto.DriverDTO;
import com.f1project.model.dto.RaceDTO;
import com.f1project.model.dto.TrackDTO;
import com.f1project.model.request.RaceRequest;
import com.f1project.service.DriverService;
import com.f1project.service.RaceService;
import com.f1project.service.TrackService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/races")
@ConditionalOnProperty(name = "app.enable-test-endpoints", havingValue = "true")
public class RaceTestController {
	
	private RaceService raceService;
	private DriverService driverService;
	private TrackService trackService;
	private CentralMapper mapper;
	
	@GetMapping("/update/{raceId}")
	public String showRaceEditForm(@PathVariable("raceId") Long raceId, Model model) {
		List<TrackDTO> tracks = mapper.tracks2DTOList(this.trackService.findAllTracksOrderByNameAsc());
		List<DriverDTO> drivers = mapper.drivers2DTOList(this.driverService.findAllDriversOrderByNameAsc());
		RaceDTO race = mapper.race2DTO(this.raceService.findRaceById(raceId));
		
		model.addAttribute("race", race);
		model.addAttribute("tracks", tracks);
		model.addAttribute("drivers", drivers);
		
		return "races/update";
	}
	
	@PostMapping("/update")
	public String editRace(RaceRequest raceRequest) {
		this.raceService.updateRace(raceRequest);
		
		return "redirect:/races";
	}
	
	@GetMapping("/delete/all")
	public String deleteAllRaces() {
		this.raceService.deleteAllRaces();
		
		return "redirect:/races";
	}
}
