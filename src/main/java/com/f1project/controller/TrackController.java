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
import com.f1project.model.dto.CountryDTO;
import com.f1project.model.dto.TrackDTO;
import com.f1project.model.request.TrackRequest;
import com.f1project.service.CountryService;
import com.f1project.service.TrackService;
import com.f1project.utils.SortList;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/tracks")
public class TrackController {
	
	private TrackService trackService;
	private CountryService countryService;
	private CentralMapper mapper;
	
	// SHOW OPERATION
	
	@GetMapping
	public String showTracks(Model model) {
		List<TrackDTO> tracks = mapper.tracks2DTOList(this.trackService.findAllTracksOrderByNameAsc());
		
		model.addAttribute("tracks", tracks);
		model.addAttribute("activePage", "tracks");
		
		return "tracks/list";
	}
	
	// CREATE OPERATION
	
	@GetMapping("/create")
	public String showTrackCreationForm(Model model) {
		List<CountryDTO> countries = mapper.countries2DTOList(this.countryService.findAllCountriesOrderByBrazilianAsc());
				
		model.addAttribute("countries", countries);
		model.addAttribute("activePage", "tracks");
				
		return "tracks/create";
	}
			
	@PostMapping("/create")
	public String createTrack(TrackRequest trackRequest) {
		this.trackService.saveTrack(trackRequest);
				
		return "redirect:/tracks";
	}
}
