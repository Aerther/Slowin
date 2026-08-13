package com.f1project.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.f1project.client.ApiClient;
import com.f1project.mapper.CentralMapper;
import com.f1project.model.entity.Driver;
import com.f1project.model.entity.Race;
import com.f1project.model.entity.RaceEvent;
import com.f1project.model.entity.RaceResult;
import com.f1project.model.entity.Team;
import com.f1project.model.entity.Track;
import com.f1project.model.entity.Weather;
import com.f1project.model.enums.DriverStatus;
import com.f1project.model.enums.EventType;
import com.f1project.model.enums.Mistake;
import com.f1project.model.enums.RaceStatus;
import com.f1project.model.enums.Tyre;
import com.f1project.model.enums.WeatherCondition;
import com.f1project.repository.RaceRepository;
import com.f1project.service.DriverService;
import com.f1project.service.RaceEventService;
import com.f1project.service.RaceService;
import com.f1project.service.SimulationService;
import com.f1project.service.TrackService;
import com.f1project.simulation.SimulationCalculator;
import com.f1project.simulation.SimulationStatus;
import com.f1project.utils.StringFormatter;
import com.f1project.utils.LapCondition;
import com.f1project.utils.LapTimeBreakDown;
import com.f1project.utils.RaceRules;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SimulationServiceImpl implements SimulationService {
	private RaceService raceService;
	private RaceEventService raceEventService;
	
	private SimulationStatus simulationStatus;
	private SimulationCalculator simulationCalculator;

	@Override
	public Race simulateRace(Long raceId, int laps) {
		Race race = this.raceService.findRaceById(raceId);
		Weather weather = race.getWeather();
		RaceRules raceRules = race.getRaceRules();
		
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
		
		WeatherCondition weatherCondition = weather.getWeatherCondition();
		
		Track track = race.getTrack();
		double trackTime = track.getFastestTime();
		
		List<RaceResult> raceResults = race.getResults();
		
		String message = null;
		List<RaceEvent> raceEventsToSave = new ArrayList<>();
		
		RaceEvent fastestLapEvent = null;
		
		for (int currentLap = 0; currentLap < laps; currentLap++) {
			
			int currentRealLap = currentLap + lapsDone + 1;
	        
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
	            int driverLevel = raceResult.getDriver().getLevel();

	            int tyreUsage = raceResult.getTyreUsage();
	            
	            LapTimeBreakDown breakdown = null;
	            double lapTime = 0;

	            boolean isTyreFlat = this.simulationStatus.isTyreFlat(raceRules, tyreUsage);
	            boolean isTyreWrong = this.simulationStatus.isTyreWrong(weatherCondition, tyre);
	            boolean isPitting = this.simulationStatus.isDriverPitting(raceRules, tyreUsage, isTyreWrong, isTyreFlat);
	            boolean isRetiring = this.simulationStatus.isDriverRetiring(raceRules, isTyreWrong, team);
	            boolean isSafetyCarOn = this.simulationStatus.isSafetyCarOn(raceStatus);
	            List<Mistake> mistakesDone = this.simulationStatus.getDriverMistakesDone(raceStatus, isTyreWrong, driverLevel);

	            LapCondition lapCondition = new LapCondition(
	                isPitting, isTyreWrong, isTyreFlat, isRetiring, isSafetyCarOn, mistakesDone
	            );

	            if(lapCondition.isSafetyCarOn()) {
	            	breakdown = this.simulateLapSafetyCarOnTime(raceResult, raceStatus, trackTime, lapCondition, team);
	            } else {
	            	breakdown = this.simulateLap(raceResult, raceStatus, trackTime, lapCondition, team);
	            }
	            
	            lapTime = breakdown.getTotalLapTime();

	            raceResult.setRaceTime(raceResult.getRaceTime() + lapTime);
	            raceResult.setCurrentLap(lapTime);

	            if (lapTime < raceResult.getFastestLap() || raceResult.getFastestLap() == 0) {
	                raceResult.setFastestLap(lapTime);
	                raceResult.setFastestLapTime(StringFormatter.formatLapTime(lapTime));
	            }
	            
	            if (lapTime < raceFastestLap) {
	                raceFastestLap = lapTime;
	                
	                message = StringFormatter.formatFastestLap(raceResult, breakdown);
	                fastestLapEvent = new RaceEvent(null, race, EventType.FASTEST_LAP, raceResult, currentRealLap, message);
	            }
	            
	            if(raceRules.isDriverTyreWearEnabled()) {
	            	tyreUsage = Math.max(0, tyreUsage - this.simulationCalculator.useTyre(raceStatus, tyre, team));
	            }
	            
	            raceResult.setStint(raceResult.getStint() + 1);

	            if (lapCondition.isPitting()) {
	                tyreUsage = 100;
	                raceResult.setPitStopQuantity(raceResult.getPitStopQuantity() + 1);
	                raceResult.setStint(0);
	                raceResult.setDriverStatus(DriverStatus.PITTED);
	                raceResult.setTyre(Tyre.chooseRandomTyre(weatherCondition));
	                
	                message = StringFormatter.formatPitStop(raceResult, breakdown);
	                raceEventsToSave.add(new RaceEvent(null, race, EventType.PIT_STOP, raceResult, currentRealLap, message));
	            }

	            if (lapCondition.isRetiring()) {
	                raceResult.setDriverStatus(DriverStatus.RETIRED);
	                raceResult.setLapRetired(currentRealLap);
	                
	                if (this.simulationStatus.isSafetyCarComing(raceRules)) {
	                    safetyCarTriggeredThisLap = true;
	                }
	                
	                message = StringFormatter.formatRetirement(raceResult);
	                raceEventsToSave.add(new RaceEvent(null, race, EventType.RETIREMENT, raceResult, currentRealLap, message));
	            }
	            
	            if(!mistakesDone.isEmpty()) {
	            	for(Mistake mistake : mistakesDone) {
	            		message = StringFormatter.formatDriverMistake(raceResult, mistake);
	            		raceEventsToSave.add(new RaceEvent(null, race, EventType.DRIVER_MISTAKE, raceResult, currentRealLap, message));
	            	}
	            }

	            raceResult.setTyreUsage(tyreUsage);
	            raceResult.setCurrentLapTime(StringFormatter.formatLapTime(lapTime));
	            raceResult.setTotalRaceTime(StringFormatter.formatLapTime(raceResult.getRaceTime()));
	        }
	        
	        if(fastestLapEvent != null) {
                raceEventsToSave.add(fastestLapEvent);
	        }
	        
	        if(raceStatus == RaceStatus.SAFETYCAR) {
	        	this.compactDriversUnderSafetyCar(raceResults);
	        }

	        if (safetyCarTriggeredThisLap) {
	        	raceStatus = RaceStatus.getRandomSafetyStatus(raceStatus);
	            
	            lapsSafetyCarDuration = this.simulationCalculator.getDurationOfSafetyCarInLaps(raceStatus);
	        }
	        
	        if(this.simulationStatus.isChangingWeather(raceRules, weatherCondition, weather)) {
	        	weatherCondition = this.simulationStatus.getNextWeather(weatherCondition, weather);
	        	
	        	message = StringFormatter.formatWeatherChanged(weatherCondition);
	        	raceEventsToSave.add(new RaceEvent(null, race, EventType.WEATHER_CHANGED, null, currentRealLap, message));
	        }
	        
	        if(this.simulationStatus.isFanOnTrackAndGotObliterated(raceRules)) {
	        	raceStatus = RaceStatus.FANINVASION;
	        	
	        	Driver driverWhoHitFan = this.simulationStatus.getRandomDriver(raceResults);
	        	race.setDriverWhoHitFan(driverWhoHitFan);
	        	race.setLapsDone(currentRealLap);
	        	
	        	message = StringFormatter.formatFanInvasion(driverWhoHitFan);
	        	raceEventsToSave.add(new RaceEvent(null, race, EventType.FAN_INVASION, null, currentRealLap, message));
	        	
	        	break;
	        }
	        
	        this.updateDriversPositions(raceResults);
	    }
		
		List<RaceResult> activeResults = this.orderRaceResultsByRaceTime(raceResults);
		
		double firstTotalTime = 0;
		
		if(!activeResults.isEmpty()) {
			firstTotalTime = activeResults.get(0).getRaceTime();
			
			activeResults.get(0).setDifference(0);
			activeResults.get(0).setDifferenceToFirst(0);
			activeResults.get(0).setDifferenceToFirstTime(StringFormatter.formatLapTime(0));
			activeResults.get(0).setDifferenceTime(StringFormatter.formatLapTime(0));
		}
		
		for(int i = 1; i < activeResults.size(); i++) {
			RaceResult raceResult = activeResults.get(i);
			RaceResult driverInFront = activeResults.get(i - 1);
			
			double difference = raceResult.getRaceTime() - driverInFront.getRaceTime();
			raceResult.setDifference(difference);
			
			raceResult.setDifferenceTime("+" + StringFormatter.formatLapTime(difference));
			
			double differenceToFirst = raceResult.getRaceTime() - firstTotalTime;
			raceResult.setDifferenceToFirst(differenceToFirst);
			
			raceResult.setDifferenceToFirstTime("+" + StringFormatter.formatLapTime(differenceToFirst));
			
			int calc = (int) (differenceToFirst / trackTime);
			
			if(calc == 1) {
				raceResult.setDifferenceToFirstTime("+" + calc + " Lap");
			}
			
			if(calc > 1) {
				raceResult.setDifferenceToFirstTime("+" + calc + " Laps");
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
        
        weather.setWeatherCondition(weatherCondition);
	
		race.setResults(raceResults);
		race.setRaceStatus(raceStatus);
		race.setFastestLap(raceFastestLap);
        race.setLapsSafetyCarDuration(lapsSafetyCarDuration);
        race.setWeather(weather);
		
		if(race.getLapsQuantity() <= race.getLapsDone()) {
			race.setRaceStatus(RaceStatus.FINISHED);
		}
		
		this.raceEventService.saveAllRaceEvents(raceEventsToSave);
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
	            current.setTotalRaceTime(StringFormatter.formatLapTime(newRaceTime));
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
	 
	public LapTimeBreakDown simulateLapSafetyCarOnTime(RaceResult raceResult, RaceStatus raceStatus, double trackFastestTime, LapCondition lapCondition, Team team) {
		Driver driver = raceResult.getDriver();
		
		int level = driver.getLevel();
		int tyreUsage = raceResult.getTyreUsage();
		
		var breakdown = new LapTimeBreakDown();
		breakdown.setBaseTrackTime(trackFastestTime);
		breakdown.setSafetyCarLoss(this.simulationCalculator.calculateSafetyCarOnTime());
		
		if(lapCondition.isTyreWrong()) {
			breakdown.setTyreWrongLoss(this.simulationCalculator.calculateTyreWrongTime(raceStatus));
		}
		
		if(lapCondition.isTyreFlat()) {
			breakdown.setTyreFlatLoss(this.simulationCalculator.calculateTyreFlatTime(raceStatus));
		}
		
		if(lapCondition.isPitting()) {
			breakdown.setPitStopLoss(this.simulationCalculator.calculatePitStopTime(raceStatus, team));
		}
		
		return breakdown;
	}
	
	private LapTimeBreakDown simulateLap(RaceResult raceResult, RaceStatus raceStatus, double trackFastestTime, LapCondition lapCondition, Team team) {
		Driver driver = raceResult.getDriver();
		
		int level = driver.getLevel();
		int tyreUsage = raceResult.getTyreUsage();
		Tyre tyre = raceResult.getTyre();
		
		var breakdown = new LapTimeBreakDown();
		breakdown.setBaseTrackTime(trackFastestTime);
		
		breakdown.setLapVariation(this.simulationCalculator.calculateLapVariation(level));
		breakdown.setDriverEngineVariation(this.simulationCalculator.calculateDriverAndEngineVariation(raceStatus, level, team));
		breakdown.setDriverMistakeLoss(this.simulationCalculator.calculateDriverMistakeTimeLoss(lapCondition));
		breakdown.setTyreUsageLoss(this.simulationCalculator.calculateTyreUsageTimeLoss(tyreUsage));
		breakdown.setDriverMistakeLoss(this.simulationCalculator.calculateDriverMistakeTimeLoss(lapCondition));
		breakdown.setTyreTypeTime(this.simulationCalculator.calculateTyreTypeTime(tyre, lapCondition));
		
		if(lapCondition.isTyreWrong()) {
			breakdown.setTyreWrongLoss(this.simulationCalculator.calculateTyreWrongTime(raceStatus));
		}
		
		if(lapCondition.isTyreFlat()) {
			breakdown.setTyreFlatLoss(this.simulationCalculator.calculateTyreFlatTime(raceStatus));
		}
		
		if(lapCondition.isPitting()) {
			breakdown.setPitStopLoss(this.simulationCalculator.calculatePitStopTime(raceStatus, team));
		}
		
		if(lapCondition.isSafetyCarOn()) {
			breakdown.setSafetyCarLoss(this.simulationCalculator.calculateSafetyCarOnTime());
		}
		
		return breakdown;
	}

	@Override
	public Race finishRace(Long raceId) {
		Race race = this.raceService.findRaceById(raceId);
		
		int rLaps = race.getLapsQuantity() - race.getLapsDone();
		
		Race savedRace = this.simulateRace(raceId, rLaps + 1);
		
		return savedRace;
	}
}

