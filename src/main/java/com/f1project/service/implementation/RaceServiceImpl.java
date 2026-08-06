package com.f1project.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.f1project.client.ApiClient;
import com.f1project.exception.ResourceNotFoundException;
import com.f1project.mapper.CentralMapper;
import com.f1project.model.dto.RaceDTO;
import com.f1project.model.entity.Driver;
import com.f1project.model.entity.Race;
import com.f1project.model.entity.RaceResult;
import com.f1project.model.entity.Track;
import com.f1project.model.entity.Weather;
import com.f1project.model.enums.DriverStatus;
import com.f1project.model.enums.RaceStatus;
import com.f1project.model.enums.Tyre;
import com.f1project.model.request.RaceRequest;
import com.f1project.repository.CountryRepository;
import com.f1project.repository.DriverRepository;
import com.f1project.repository.RaceRepository;
import com.f1project.repository.RaceResultRepository;
import com.f1project.repository.TrackRepository;
import com.f1project.service.DriverService;
import com.f1project.service.RaceService;
import com.f1project.service.TrackService;
import com.f1project.utils.RaceRules;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Service
public class RaceServiceImpl implements RaceService {
	
	private RaceRepository raceRepo;
	private TrackService trackService;
	private DriverService driverService;
	
	private CentralMapper mapper;
	private ApiClient client;

	@Override
	public List<Race> findAllRaces() {
		return this.raceRepo.findAll();
	}

	@Override
	public Race findRaceById(Long id) {
		return this.raceRepo.findById(id).orElseThrow(() -> {throw new ResourceNotFoundException("A RACE with ID=" + id + " doesn't exists");});
	}

	@Override
	public Race saveRace(RaceRequest raceRequest) {
		Long trackId = raceRequest.getTrackId();
		Track track = this.trackService.findTrackById(trackId);
		
		Weather weather = mapper.DTO2weather(client.getWeatherCity(track.getLatitude(), track.getLongitude()));
		weather.setWeatherCondition();
		
		RaceRules raceRules = mapper.request2RaceRules(raceRequest);
		
		Race race = mapper.request2Race(raceRequest);
		race.setId(null);
		race.setTrack(track);
		race.setWeather(weather);
		race.setDateCreated(LocalDateTime.now());
		race.setLapsDone(0);
		race.setRaceStatus(RaceStatus.CREATED);
		race.setRaceRules(raceRules);
		
		List<RaceResult> raceResults = new ArrayList<>();
		
		for(Long driverId : raceRequest.getDriversId()) {
			Driver driver = this.driverService.findDriverById(driverId);
			
			RaceResult raceResult = new RaceResult();
			raceResult.setDriver(driver);
			raceResult.setRace(race);
			raceResult.setPosition(1);
			raceResult.setTyre(Tyre.chooseRandomTyre(weather.getWeatherCondition()));
			raceResult.setDriverStatus(DriverStatus.RACING);
			raceResult.setTyreUsage(100);
			
			raceResults.add(raceResult);
		}
		
		race.setResults(raceResults);

		Race savedRace = this.raceRepo.save(race);
		
		return savedRace;
	}

	@Override
	public Race updateRace(RaceRequest raceRequest) {
		return null;
	}

	@Override
	public void deleteRaceById(Long id) {
		this.findRaceById(id);
		
		this.raceRepo.deleteById(id);
	}

	@Override
	public void deleteAllRaces() {
		this.raceRepo.deleteAll();
	}

	@Override
	public Race updateRace(Race race) {
		return this.raceRepo.save(race);
	}

}
