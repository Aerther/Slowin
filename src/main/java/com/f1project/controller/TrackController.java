package com.f1project.controller;

import java.util.List;

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
import com.f1project.request.TrackRequest;
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
		List<TrackDTO> tracksDTO = SortList.sortTracksByName(mapper.tracks2DTOList(this.trackService.findAllTracks()));
		
		model.addAttribute("tracks", tracksDTO);
		model.addAttribute("activePage", "tracks");
		
		return "tracks/list";
	}
	
	// CREATE OPERATION
	
	@GetMapping("/create")
	public String showTrackCreationForm(Model model) {
		List<CountryDTO> countriesDTO = SortList.sortCountriesByName(mapper.countries2DTOList(this.countryService.findAllCountries()));
		
		model.addAttribute("countries", countriesDTO);
		
		return "tracks/create";
	}
	
	@PostMapping("/create")
	public String createTrack(TrackRequest trackRequest) {
		this.trackService.saveTrack(trackRequest);
		
		return "redirect:/tracks";
	}
	
	@GetMapping("/create/txt")
	public String createTracksBasedTxt() {
		this.trackService.saveTracksFromTxt();
		
		return "redirect:/tracks";
	}
	
	// UPDATE OPERATION
	
	@GetMapping("/update/{trackId}")
	public String showTrackEditForm(@PathVariable("trackId") Long trackId, Model model) {
		List<CountryDTO> countriesDTO = mapper.countries2DTOList(this.countryService.findAllCountries());
		TrackDTO trackDTO = mapper.track2DTO(this.trackService.findTrackById(trackId));
		
		model.addAttribute("track", trackDTO);
		model.addAttribute("countries", countriesDTO);
		
		return "tracks/update";
	}
	
	@PostMapping("/update")
	public String updateTrack(TrackRequest trackRequest) {
		this.trackService.updateTrack(trackRequest);
		
		return "redirect:/tracks";
	}
	
	// DELETE OPERATION
	
	@GetMapping("/delete")
	public String deleteTrack(@RequestParam("trackId") Long trackId) {
		this.trackService.deleteTrack(trackId);
		
		return "redirect:/tracks";
	}
	
	@GetMapping("/delete/all")
	public String deleteAllTracks() {
		this.trackService.deleteAllTracks();
		
		return "redirect:/tracks";
	}
}
