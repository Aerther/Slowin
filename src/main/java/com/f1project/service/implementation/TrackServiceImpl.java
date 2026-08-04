package com.f1project.service.implementation;

import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f1project.exception.ResourceNotFoundException;
import com.f1project.mapper.CentralMapper;
import com.f1project.model.dto.TrackDTO;
import com.f1project.model.entity.Country;
import com.f1project.model.entity.Track;
import com.f1project.repository.CountryRepository;
import com.f1project.repository.TrackRepository;
import com.f1project.request.TrackRequest;
import com.f1project.service.CountryService;
import com.f1project.service.TrackService;
import com.f1project.utils.FormatUtils;
import com.f1project.utils.ReadFile;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TrackServiceImpl implements TrackService {
	
	private CountryService countryService;
	private CentralMapper mapper;
	private TrackRepository trackRepo;
	
	@Override
	public List<Track> findAllTracks() {
		List<Track> tracks = this.trackRepo.findAll();
		
		return tracks;
	}

	@Override
	public Track findTrackById(Long id) {
		return this.trackRepo.findById(id).orElseThrow(() -> {throw new ResourceNotFoundException("A TRACK with ID=" + id + " doesn't exists");});
	}

	@Override
	public Track saveTrack(TrackRequest trackRequest) {
		Long countryId = trackRequest.getCountryId();
		Country country = this.countryService.findCountryById(countryId);
		
		Track track = mapper.request2Track(trackRequest);
		track.setCountry(country);
		
		Track savedTrack = this.trackRepo.save(track);
		
		return savedTrack;
	}

	@Override
	public void deleteTrack(Long id) {
		this.findTrackById(id);
		
		this.trackRepo.deleteById(id);
	}

	@Override
	public void deleteAllTracks() {
		this.trackRepo.deleteAll();
	}

	@Override
	public Track updateTrack(TrackRequest trackRequest) {
		this.findTrackById(trackRequest.getId());
		
		Long countryId = trackRequest.getCountryId();
		Country country = this.countryService.findCountryById(countryId);		
		
		Track track = mapper.request2Track(trackRequest);
		track.setId(trackRequest.getId());
		track.setCountry(country);
		
		Track savedTrack = this.trackRepo.save(track);
		
		return savedTrack;
	}

	@Override
	public List<Track> saveTracksFromTxt() {
		List<TrackDTO> tracksDTO = ReadFile.readTracksFile();
		
		List<Track> tracks = tracksDTO.stream().map(mapper::DTO2track).collect(Collectors.toList());
		
		tracks.forEach(track -> {
			String countryBrazilian = track.getCountry().getBrazilian();
			
			Country country = this.countryService.findCountryByBrazilian(countryBrazilian);

			track.setCountry(country);
		});
		
		List<Track> savedTracks = this.trackRepo.saveAll(tracks);
		
		return savedTracks;
	}
	
}
