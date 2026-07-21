package com.f1project.service.implementation;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.f1project.client.ApiClient;
import com.f1project.helper.CentralMapper;
import com.f1project.helper.SimulationRandom;
import com.f1project.model.entity.Driver;
import com.f1project.model.entity.Race;
import com.f1project.model.entity.RaceResult;
import com.f1project.model.entity.Track;
import com.f1project.repository.RaceRepository;
import com.f1project.service.DriverService;
import com.f1project.service.RaceService;
import com.f1project.service.SimulationService;
import com.f1project.service.TrackService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SimulationServiceImpl implements SimulationService {
	private RaceService raceService;
	
	private SimulationRandom simulationRandom;

	@Override
	public Race simulateRace(Long raceId, int laps) {
		Race race = this.raceService.findRaceById(raceId);
		
		Track track = race.getTrack();
		double trackTime = track.getFastestTime();
		
		List<RaceResult> raceResults = race.getResults();
		
		for(RaceResult raceResult : raceResults) {
			Driver driver = raceResult.getDriver();
			
			double totalTime = 0;
			
			for(int i = 0; i < laps; i++) {
				totalTime += this.simulateLap(driver, trackTime);
			}
			
			raceResult.setRaceTime(totalTime);
		}
		
		raceResults.sort((a, b) -> Double.compare(a.getRaceTime(), b.getRaceTime()));
		
		for(int i = 0; i < raceResults.size(); i++) {
			raceResults.get(i).setPosition(i + 1);
		}
		
		Race savedRace = this.raceService.updateRace(race);
		
		return savedRace;
	}
	
	private double simulateLap(Driver driver, double trackFastestTime) {
		int level = driver.getLevel();
		
		double lapTime = trackFastestTime;
		
		lapTime += this.simulationRandom.calculateLapVariation();
		lapTime += this.simulationRandom.calculateDriverLevelVariation(level);
		lapTime += this.simulationRandom.calculateDriverMistake(level);
		
		return lapTime;
	}
}

