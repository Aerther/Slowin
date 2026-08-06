package com.f1project.simulation;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.f1project.model.entity.Team;
import com.f1project.model.entity.Weather;
import com.f1project.model.enums.Mistake;
import com.f1project.model.enums.RaceStatus;
import com.f1project.model.enums.Tyre;
import com.f1project.model.enums.WeatherCondition;
import com.f1project.utils.RaceRules;

@Service
public class SimulationStatus {
	private Random random = new Random();
	
	private double getRandomNum() {
		return random.nextDouble() * 100;
	}
	
	public boolean isChangingWeather(RaceRules raceRules, WeatherCondition currentCondition, Weather weather) {
		if(!raceRules.isWeatherChangeEnabled()) return false;
		
		double baseChance = 4;
	    double windBonus = weather.getWindSpeed() * 0.1;
	    
	    double changeProbability = (baseChance + windBonus);

	    return this.getRandomNum() < changeProbability;
	}
	
	public WeatherCondition getNextWeather(WeatherCondition currentCondition, Weather weather) {
		double rNum = this.getRandomNum();
		
		List<WeatherCondition> possibleConditions = currentCondition.getNextWeatherPossibleConditions();
		int probability = currentCondition.getProbabilityToChangeToFirst();
		
		if(possibleConditions.isEmpty()) return WeatherCondition.SUNNY;
		
		if(possibleConditions.size() == 1) return possibleConditions.get(0);
		
		return probability > rNum ? possibleConditions.get(0) : possibleConditions.get(1);
	}
	
	public boolean isFanOnTrackAndGotObliterated(RaceRules raceRules) {
		if(!raceRules.isFanInvasionEnabled()) return false;
		
		return this.getRandomNum() < 0.0005;
	}
	
	public boolean isDriverRetiring(RaceRules raceRules, boolean isTyreWrong, Team team) {
		if(!raceRules.isDriverRetirementEnabled()) return false;
		
		double multiplier = isTyreWrong ? 2.0 : 1.0;
	    
	    double baseRetirementChance = 0.07;
	    
	    double engineFailureChance = (100.0 - team.getMotorReliability()) / 500.0;
	    
	    double totalChance = (baseRetirementChance + engineFailureChance) * multiplier;
	    
	    return this.getRandomNum() < totalChance;
	}
	
	public boolean isSafetyCarComing(RaceRules raceRules) {
		if(!raceRules.isSafetyCarEnabled()) return false;
		
		return this.getRandomNum() < 60;
	}
	
	public boolean isDriverPitting(RaceRules raceRules, int tyreUsage, boolean isTyreWrong, boolean isTyreFlat) {
		if(!raceRules.isDriverPittingEnabled()) return false;
		
		if(isTyreFlat) return true;
		
		if(isTyreWrong && this.getRandomNum() < 85) return true;
		
		if(tyreUsage > 40) return false;
		
		if(tyreUsage > 20 && tyreUsage <= 40 && this.getRandomNum() < 20) {
			return true;
		}
		
		if(tyreUsage > 10 && tyreUsage <= 20 && this.getRandomNum() < 40) {
			return true;
		}
		
		if(tyreUsage <= 10 && this.getRandomNum() < 80) {
			return true;
		}
		
		return false;
	}
	
	public boolean isTyreWrong(WeatherCondition weatherCondition, Tyre tyre) {
		return weatherCondition.isDry() != tyre.isDryTyre(); 
	}
	
	public boolean isTyreFlat(RaceRules raceRules, int tyreUsage) {
		if(!raceRules.isDriverRetirementEnabled()) return false;
		
		if(tyreUsage > 30 && this.getRandomNum() < 0.2) {
			return true;
		}
		
		if(tyreUsage <= 30 && tyreUsage > 10 && this.getRandomNum() < 2) {
			return true;
		}
		
		if(tyreUsage <= 10 && this.getRandomNum() < 8) {
			return true;
		}
		
		return false;
	}
	
	public boolean isSafetyCarOn(RaceStatus raceStatus) {
		return raceStatus.isSafety();
	}
	
	public static boolean didDriverMadeMistake(Mistake mistake, double multiplier) {
		Random random = new Random();
		
		return mistake.getChance() * multiplier > random.nextDouble() * 100;
	}
}
