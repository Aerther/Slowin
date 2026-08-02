package com.f1project.service.implementation;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.f1project.client.ApiClient;
import com.f1project.model.entity.Driver;
import com.f1project.model.entity.Race;
import com.f1project.model.entity.RaceResult;
import com.f1project.model.entity.Team;
import com.f1project.model.entity.Track;
import com.f1project.repository.RaceRepository;
import com.f1project.service.DriverService;
import com.f1project.service.RaceService;
import com.f1project.service.SimulationService;
import com.f1project.service.TrackService;
import com.f1project.utils.FormatUtils;
import com.f1project.utils.LapCondition;
import com.f1project.utils.enums.DriverStatus;
import com.f1project.utils.enums.RaceStatus;
import com.f1project.utils.enums.Tyre;
import com.f1project.utils.enums.WeatherCondition;
import com.f1project.utils.mapper.CentralMapper;
import com.f1project.utils.simulation.SimulationCalculator;
import com.f1project.utils.simulation.SimulationStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SimulationServiceImpl implements SimulationService {
	private RaceService raceService;
	
	private SimulationStatus simulationStatus;
	private SimulationCalculator simulationCalculator;

	@Override
	public Race simulateRace(Long raceId, int laps) {
		Race race = this.raceService.findRaceById(raceId);
		double raceFastestLap = (race.getFastestLap() > 0) ? race.getFastestLap() : Double.MAX_VALUE;
		RaceStatus raceStatus = race.getRaceStatus();
		int lapsSafetyCarDuration = race.getLapsSafetyCarDuration();
		
		if(race.getRaceStatus() == RaceStatus.FINISHED) return null;
		
		if(laps > 0 && lapsSafetyCarDuration == 0) {
			race.setRaceStatus(RaceStatus.ONGOING);
		}
		
		if(laps + race.getLapsDone() >= race.getLapsQuantity()) {
			laps = race.getLapsQuantity() - race.getLapsDone();
		}
		
		int lapsDone = race.getLapsDone();
		race.setLapsDone(laps + lapsDone);
		
		WeatherCondition weatherCondition = race.getWeather().getWeatherCondition();
		
		Track track = race.getTrack();
		double trackTime = track.getFastestTime();
		
		List<RaceResult> raceResults = race.getResults();
		
		for (int currentLap = 0; currentLap < laps; currentLap++) {
	        
	        if (raceStatus.isSafety()) {
	            if (lapsSafetyCarDuration > 0) {
	                lapsSafetyCarDuration = lapsSafetyCarDuration - 1;
	            }
	            
	            if (lapsSafetyCarDuration == 0) {
	                raceStatus = RaceStatus.ONGOING;
	            }
	        }

	        boolean safetyCarTriggeredThisLap = false;

	        for (RaceResult raceResult : raceResults) {
	            if (raceResult.getDriverStatus() == DriverStatus.RETIRED) continue;

	            raceResult.setDriverStatus(DriverStatus.RACING);

	            Team team = raceResult.getDriver().getTeam();
	            Tyre tyre = raceResult.getTyre();

	            int tyreUsage = raceResult.getTyreUsage();
	            
	            double lapTime = 0;

	            boolean isTyreFlat = this.simulationStatus.isTyreFlat(tyreUsage);
	            boolean isTyreWrong = this.simulationStatus.isTyreWrong(weatherCondition, tyre);
	            boolean isPitting = this.simulationStatus.isDriverPitting(tyreUsage, isTyreWrong, isTyreFlat);
	            boolean isRetiring = this.simulationStatus.isDriverRetiring(isTyreWrong, team);
	            boolean isSafetyCarOn = this.simulationStatus.isSafetyCarOn(raceStatus);

	            LapCondition lapCondition = new LapCondition(
	                isPitting, isTyreWrong, isTyreFlat, isRetiring, isSafetyCarOn
	            );

	            if(lapCondition.isSafetyCarOn()) {
	            	lapTime = this.simulateLapSafetyCarOnTime(raceResult, raceStatus, trackTime, lapCondition, team);
	            } else {
	            	lapTime = this.simulateLap(raceResult, raceStatus, trackTime, lapCondition, team);
	            }

	            raceResult.setRaceTime(raceResult.getRaceTime() + lapTime);
	            raceResult.setCurrentLap(lapTime);

	            if (lapTime < raceResult.getFastestLap() || raceResult.getFastestLap() == 0) {
	                raceResult.setFastestLap(lapTime);
	                raceResult.setFastestLapTime(FormatUtils.formatLapTime(lapTime));
	            }
	            
	            if (lapTime < raceFastestLap) {
	                raceFastestLap = lapTime;
	            }

	            tyreUsage = Math.max(0, tyreUsage - this.simulationCalculator.useTyre(raceStatus, tyre, team));
	            raceResult.setStint(raceResult.getStint() + 1);

	            if (lapCondition.isPitting()) {
	                tyreUsage = 100;
	                raceResult.setPitStopQuantity(raceResult.getPitStopQuantity() + 1);
	                raceResult.setStint(0);
	                raceResult.setDriverStatus(DriverStatus.PITTED);
	                raceResult.setTyre(Tyre.chooseRandomTyre(weatherCondition));
	            }

	            if (lapCondition.isRetiring()) {
	                raceResult.setDriverStatus(DriverStatus.RETIRED);
	                raceResult.setLapRetired(currentLap + lapsDone);
	                
	                if (this.simulationStatus.isSafetyCarComing()) {
	                    safetyCarTriggeredThisLap = true;
	                }
	            }

	            raceResult.setTyreUsage(tyreUsage);
	            raceResult.setCurrentLapTime(FormatUtils.formatLapTime(lapTime));
	            raceResult.setTotalRaceTime(FormatUtils.formatLapTime(raceResult.getRaceTime()));
	        }
	        
	        if(raceStatus == RaceStatus.SAFETYCAR) {
	        	this.compactDriversUnderSafetyCar(raceResults);
	        }

	        if (safetyCarTriggeredThisLap) {
	        	raceStatus = RaceStatus.getRandomSafetyStatus(raceStatus);
	            
	            lapsSafetyCarDuration = 11;
	        }
	        
	        this.updateDriversPositions(raceResults);
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
		}
		
		for(int i = 1; i < activeResults.size(); i++) {
			RaceResult raceResult = activeResults.get(i);
			RaceResult driverInFront = activeResults.get(i - 1);
			
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
			
			raceResult.setDifference(0);
			raceResult.setDifferenceToFirst(0);
			raceResult.setDifferenceToFirstTime("DNF");
			raceResult.setDifferenceTime("DNF");
		}
		
		race.getResults().clear();
        race.getResults().addAll(activeResults);
        race.getResults().addAll(retiredResults);
	
		race.setResults(raceResults);
		race.setRaceStatus(raceStatus);
		race.setFastestLap(raceFastestLap);
        race.setLapsSafetyCarDuration(lapsSafetyCarDuration);
		
		if(race.getLapsQuantity() <= race.getLapsDone()) {
			race.setRaceStatus(RaceStatus.FINISHED);
		}
		
		Race savedRace = this.raceService.updateRace(race);
		
		return savedRace;
	}
	
	private void updateDriversPositions(List<RaceResult> raceResults) {
	    List<RaceResult> activeResults = this.orderRaceResultsByRaceTime(raceResults);
	    List<RaceResult> retiredResults = this.orderRaceResultsRetiredByRaceTime(raceResults);

	    for (int i = 0; i < activeResults.size(); i++) {
	        RaceResult result = activeResults.get(i);
	        
	        result.setLastPosition(result.getPosition());
	        
	        result.setPosition(i + 1);
	    }

	    int offset = activeResults.size();
	    for (int i = 0; i < retiredResults.size(); i++) {
	        RaceResult result = retiredResults.get(i);
	        
	        int pos = offset + i + 1;
	        
	        result.setLastPosition(result.getPosition());
	        result.setPosition(pos);
	    }

	    raceResults.clear();
	    raceResults.addAll(activeResults);
	    raceResults.addAll(retiredResults);
	}
	
	private void compactDriversUnderSafetyCar(List<RaceResult> raceResults) {
		List<RaceResult> activeResults = this.orderRaceResultsByRaceTime(raceResults);
	    
	    if (activeResults.size() <= 1) return;
	    
	    double targetGap = 0.8;

	    for (int i = 1; i < activeResults.size(); i++) {
	        RaceResult current = activeResults.get(i);
	        RaceResult carAhead = activeResults.get(i - 1);

	        if (current.getDriverStatus() == DriverStatus.PITTED) continue;

	        double currentGap = current.getRaceTime() - carAhead.getRaceTime();

	        if (currentGap > targetGap) {
	            double reduction = Math.min(currentGap - targetGap, 15.0); 
	            double newRaceTime = current.getRaceTime() - reduction;
	            
	            current.setRaceTime(newRaceTime);
	            current.setTotalRaceTime(FormatUtils.formatLapTime(newRaceTime));
	        }
	    }
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
	 
	public double simulateLapSafetyCarOnTime(RaceResult raceResult, RaceStatus raceStatus, double trackFastestTime, LapCondition lapCondition, Team team) {
		Driver driver = raceResult.getDriver();
		
		int level = driver.getLevel();
		int tyreUsage = raceResult.getTyreUsage();
		
		double lapTime = trackFastestTime;
		
		if(lapCondition.isTyreWrong()) {
			lapTime += this.simulationCalculator.calculateTyreWrongTime(raceStatus);
		}
		
		if(lapCondition.isTyreFlat()) {
			lapTime += this.simulationCalculator.calculateTyreFlatTime(raceStatus);
		}
		
		if(lapCondition.isPitting()) {
			lapTime += this.simulationCalculator.calculatePitStopTime(raceStatus);
		}
		
		if(lapCondition.isSafetyCarOn()) {
			lapTime += this.simulationCalculator.calculateSafetyCarOnTime();
		}
		
		return lapTime;
	}
	
	private double simulateLap(RaceResult raceResult, RaceStatus raceStatus, double trackFastestTime, LapCondition lapCondition, Team team) {
		Driver driver = raceResult.getDriver();
		
		int level = driver.getLevel();
		int tyreUsage = raceResult.getTyreUsage();
		
		double lapTime = trackFastestTime;
		
		lapTime += this.simulationCalculator.calculateLapVariation(level); 
		lapTime += this.simulationCalculator.calculateDriverAndEngineVariation(raceStatus, level, team);
		lapTime += this.simulationCalculator.calculateDriverMistake(raceStatus, lapCondition, level);
		lapTime += this.simulationCalculator.calculateTyreUsageTimeLoss(tyreUsage);
		
		if(lapCondition.isTyreWrong()) {
			lapTime += this.simulationCalculator.calculateTyreWrongTime(raceStatus);
		}
		
		if(lapCondition.isTyreFlat()) {
			lapTime += this.simulationCalculator.calculateTyreFlatTime(raceStatus);
		}
		
		if(lapCondition.isPitting()) {
			lapTime += this.simulationCalculator.calculatePitStopTime(raceStatus);
		}
		
		if(lapCondition.isSafetyCarOn()) {
			lapTime += this.simulationCalculator.calculateSafetyCarOnTime();
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

