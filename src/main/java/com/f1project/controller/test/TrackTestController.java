package com.f1project.controller.test;

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

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/tracks")
@ConditionalOnProperty(name = "app.enable-test-endpoints", havingValue = "true")
public class TrackTestController {
	private TrackService trackService;
	private CountryService countryService;
	private CentralMapper mapper;
		
	@GetMapping("/create/txt")
	public String createTracksBasedTxt() {
		this.trackService.saveTracksFromTxt();
		
		return "redirect:/tracks";
	}
		
	// UPDATE OPERATION
	@GetMapping("/update/{trackId}")
	public String showTrackEditForm(@PathVariable("trackId") Long trackId, Model model) {
		List<CountryDTO> countries = mapper.countries2DTOList(this.countryService.findAllCountriesOrderByBrazilianAsc());
		TrackDTO track = mapper.track2DTO(this.trackService.findTrackById(trackId));
			
		model.addAttribute("track", track);
		model.addAttribute("countries", countries);
			
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
