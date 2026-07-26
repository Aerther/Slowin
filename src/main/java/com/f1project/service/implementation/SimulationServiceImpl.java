package com.f1project.service.implementation;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.f1project.client.ApiClient;
import com.f1project.helper.CentralMapper;
import com.f1project.helper.FormatUtils;
import com.f1project.helper.Simulation;
import com.f1project.helper.enums.DriverStatus;
import com.f1project.helper.enums.RaceStatus;
import com.f1project.helper.enums.Tyre;
import com.f1project.helper.enums.WeatherCondition;
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
	
	private Simulation simulation;

	@Override
	public Race simulateRace(Long raceId, int laps) {
		Race race = this.raceService.findRaceById(raceId);
		
		if(race.getRaceStatus() == RaceStatus.FINISHED) return null;
		
		if(laps > 0) {
			race.setRaceStatus(RaceStatus.ONGOING);
		}
		
		if(laps + race.getLapsDone() >= race.getLapsQuantity()) {
			laps = race.getLapsQuantity() - race.getLapsDone();
		}
		
		race.setLapsDone(laps + race.getLapsDone());
		
		WeatherCondition weatherCondition = race.getWeather().getWeatherCondition();
		
		Track track = race.getTrack();
		double trackTime = track.getFastestTime();
		
		List<RaceResult> raceResults = race.getResults();
		
		for (RaceResult raceResult : raceResults) {
		    Driver driver = raceResult.getDriver();

		    double totalTime = raceResult.getRaceTime();
		    int tyreUsage = raceResult.getTyreUsage();
		    int pitStop = raceResult.getPitStopQuantity();
		    Tyre tyre = raceResult.getTyre();
		    DriverStatus driverStatus = raceResult.getDriverStatus();
		    
		    if(driverStatus == DriverStatus.RETIRED) continue;
		    
		    double previousFastest = raceResult.getFastestLap();
		    double bestLap = (previousFastest > 0) ? previousFastest : Double.MAX_VALUE;
		    
		    double lastSimulatedLap = 0.0;

		    for (int i = 0; i < laps; i++) {
		    	if(driverStatus == DriverStatus.RETIRED) break;
		    	
		    	driverStatus = DriverStatus.RACING;
		    	
		        boolean isPitting = this.simulation.isDriverPitting(tyreUsage);
		        boolean isRetiring = this.simulation.isDriverRetiring();
		        
		        double lapTime = this.simulateLap(driver, trackTime, tyreUsage, isPitting);
		        
		        tyreUsage = tyreUsage - this.simulation.useTyre(tyre);
		        tyreUsage = Math.max(tyreUsage, 0);
		        
		        if(isPitting) {
		        	tyreUsage = 100;
		        	pitStop += 1;
		        	
		        	driverStatus = DriverStatus.PITTED;
		        	tyre = Tyre.chooseRandomTyre(weatherCondition);
		        }
		        
		        if(isRetiring) {
		        	driverStatus = DriverStatus.RETIRED;
		        }
		        
		        totalTime += lapTime;
		        lastSimulatedLap = lapTime;

		        if (lapTime < bestLap) {
		            bestLap = lapTime;
		        }
		    }

		    raceResult.setDriverStatus(driverStatus);
		    raceResult.setTyre(tyre);
		    raceResult.setPitStopQuantity(pitStop);
		    raceResult.setTyreUsage(tyreUsage);
		    raceResult.setFastestLap(bestLap);
		    raceResult.setCurrentLap(lastSimulatedLap);
		    raceResult.setRaceTime(totalTime);

		    raceResult.setFastestLapTime(FormatUtils.formatLapTime(bestLap));
		    raceResult.setCurrentLapTime(FormatUtils.formatLapTime(lastSimulatedLap));
		    raceResult.setTotalRaceTime(FormatUtils.formatLapTime(totalTime));
		}
		
		List<RaceResult> activeResults = this.orderRaceResultsByRaceTime(raceResults);
		
		double firstTotalTime = 0;
		int lastPosition = activeResults.size();
		
		if(!activeResults.isEmpty()) {
			firstTotalTime = activeResults.get(0).getRaceTime();
			
			activeResults.get(0).setDifference(0);
			activeResults.get(0).setDifferenceToFirst(0);
			activeResults.get(0).setDifferenceToFirstTime(FormatUtils.formatLapTime(0));
			activeResults.get(0).setDifferenceTime(FormatUtils.formatLapTime(0));
			activeResults.get(0).setLastPosition(activeResults.get(0).getPosition());
			activeResults.get(0).setPosition(1);
		}
		
		for(int i = 1; i < activeResults.size(); i++) {
			RaceResult raceResult = activeResults.get(i);
			RaceResult driverInFront = activeResults.get(i - 1);
			
			raceResult.setLastPosition(raceResult.getPosition());
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
		
		List<RaceResult> retiredResults = this.orderRaceResultsRetiredByRaceTime(raceResults);
		
		for(int i = 0; i < retiredResults.size(); i++) {
			RaceResult raceResult = retiredResults.get(i);
			
			raceResult.setPosition(lastPosition + i + 1);
			raceResult.setLastPosition(lastPosition + i + 1);
			raceResult.setDifference(0);
			raceResult.setDifferenceToFirst(0);
			raceResult.setDifferenceToFirstTime("DNF");
			raceResult.setDifferenceTime("DNF");
		}
		
		race.getResults().clear();
        race.getResults().addAll(activeResults);
        race.getResults().addAll(retiredResults);
		
		race.setResults(raceResults);
		
		if(race.getLapsQuantity() <= race.getLapsDone()) {
			race.setRaceStatus(RaceStatus.FINISHED);
		}
		
		Race savedRace = this.raceService.updateRace(race);
		
		return savedRace;
	}
	
	public List<RaceResult> orderRaceResultsByRaceTime(List<RaceResult> raceResults) {
		return raceResults.stream().filter(raceResult -> raceResult.getDriverStatus() != DriverStatus.RETIRED)
			.sorted((a, b) -> Double.compare(a.getRaceTime(), b.getRaceTime()))
			.collect(Collectors.toList());
	}
	
	public List<RaceResult> orderRaceResultsRetiredByRaceTime(List<RaceResult> raceResults) {
		return raceResults.stream().filter(raceResult -> raceResult.getDriverStatus() == DriverStatus.RETIRED)
				.sorted((a, b) -> Double.compare(b.getRaceTime(), a.getRaceTime()))
				.collect(Collectors.toList());
	}
	
	public Race orderRaceResultsByRaceTime(Long raceId) {
		Race race = this.raceService.findRaceById(raceId);
		
		List<RaceResult> raceResults = race.getResults();
		
		List<RaceResult> activeResults = this.orderRaceResultsByRaceTime(raceResults);
		List<RaceResult> retiredResults = this.orderRaceResultsRetiredByRaceTime(raceResults);
		
		raceResults.clear();
		raceResults.addAll(activeResults);
		raceResults.addAll(retiredResults);
		
		race.setResults(raceResults);
		
		Race savedRace = this.raceService.updateRace(race);
		
		return savedRace;
	}
	 
	private double simulateLap(Driver driver, double trackFastestTime, int tyreUsage, boolean isPitting) {
		int level = driver.getLevel();
		
		double lapTime = trackFastestTime;
		
		lapTime += this.simulation.calculateLapVariation(); 
		lapTime += this.simulation.calculateDriverLevelVariation(level);
		lapTime += this.simulation.calculateDriverMistake(level);
		lapTime += this.simulation.calculateTyreUsage(tyreUsage);
		
		if(isPitting) {
			lapTime += this.simulation.calculatePitStopTime();
		}
		
		return lapTime;
	}

	@Override
	public Race finishRace(Long raceId) {
		Race race = this.raceService.findRaceById(raceId);
		
		int rLaps = race.getLapsQuantity() - race.getLapsDone();
		
		Race savedRace = this.simulateRace(raceId, rLaps + 1);
		
		return savedRace;
	}
}

