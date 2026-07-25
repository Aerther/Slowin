package com.f1project.service.implementation;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.f1project.client.ApiClient;
import com.f1project.helper.CentralMapper;
import com.f1project.helper.FormatUtils;
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
		race.setLapsDone(laps + race.getLapsDone());
		
		Track track = race.getTrack();
		double trackTime = track.getFastestTime();
		
		List<RaceResult> raceResults = race.getResults();
		
		for (RaceResult raceResult : raceResults) {
		    Driver driver = raceResult.getDriver();

		    double totalTime = raceResult.getRaceTime();
		    
		    double previousFastest = raceResult.getFastestLap();
		    double bestLap = (previousFastest > 0) ? previousFastest : Double.MAX_VALUE;
		    
		    double lastSimulatedLap = 0.0;

		    for (int i = 0; i < laps; i++) {
		        double lapTime = this.simulateLap(driver, trackTime);
		        totalTime += lapTime;
		        lastSimulatedLap = lapTime;

		        if (lapTime < bestLap) {
		            bestLap = lapTime;
		        }
		    }

		    raceResult.setFastestLap(bestLap);
		    raceResult.setCurrentLap(lastSimulatedLap);
		    raceResult.setRaceTime(totalTime);

		    raceResult.setFastestLapTime(FormatUtils.formatLapTime(bestLap));
		    raceResult.setCurrentLapTime(FormatUtils.formatLapTime(lastSimulatedLap));
		    raceResult.setTotalRaceTime(FormatUtils.formatLapTime(totalTime));
		}
		
		raceResults.sort((a, b) -> Double.compare(a.getRaceTime(), b.getRaceTime()));
		
		double firstTotalTime = raceResults.get(0).getRaceTime();
		
		raceResults.get(0).setDifference(0);
		raceResults.get(0).setDifferenceToFirst(0);
		raceResults.get(0).setDifferenceToFirstTime(FormatUtils.formatLapTime(0));
		raceResults.get(0).setDifferenceTime(FormatUtils.formatLapTime(0));
		raceResults.get(0).setPosition(1);
		for(int i = 1; i < raceResults.size(); i++) {
			RaceResult raceResult = raceResults.get(i);
			RaceResult driverInFront = raceResults.get(i - 1);
			
			raceResult.setPosition(i + 1);
			
			double difference = raceResult.getRaceTime() - driverInFront.getRaceTime();
			raceResult.setDifference(difference);
			
			raceResult.setDifferenceTime("+" + FormatUtils.formatLapTime(difference));
			
			double differenceToFirst = raceResult.getRaceTime() - firstTotalTime;
			raceResult.setDifferenceToFirst(differenceToFirst);
			
			raceResult.setDifferenceToFirstTime("+" + FormatUtils.formatLapTime(differenceToFirst));
			
			int calc = (int) (differenceToFirst / trackTime);
			
			if(calc == 1) {
				raceResult.setDifferenceToFirstTime("+" + calc + "Lap");
			}
			
			if(calc > 1) {
				raceResult.setDifferenceToFirstTime("+" + calc + "Laps");
			}
		}
		
		race.setResults(raceResults);
		
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

	@Override
	public Race finishRace(Long raceId) {
		Race race = this.raceService.findRaceById(raceId);
		
		int rLaps = race.getLapsQuantity() - race.getLapsDone();
		
		this.simulateRace(raceId, rLaps);
		
		Race savedRace = this.raceService.updateRace(race);
		
		return savedRace;
	}
}

