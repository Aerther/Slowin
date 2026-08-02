package com.f1project.utils.simulation;

import java.util.Random;

import com.f1project.model.entity.Team;
import com.f1project.utils.enums.Mistake;
import com.f1project.utils.enums.RaceStatus;
import com.f1project.utils.enums.Tyre;
import com.f1project.utils.enums.WeatherCondition;

public class SimulationStatus {
	private Random random = new Random();
	
	private double getRandomNum() {
		return random.nextDouble() * 100;
	}
	
	public boolean isSafetyCarComing() {
		return this.getRandomNum() < 60;
	}
	
	public boolean isDriverRetiring(boolean isTyreWrong, Team team) {
		double multiplier = isTyreWrong ? 2.0 : 1.0;
	    
	    double baseRetirementChance = 0.07; 
	    
	    double engineFailureChance = (100.0 - team.getMotorReliability()) / 500.0; 
	    
	    double totalChance = (baseRetirementChance + engineFailureChance) * multiplier;
	    
	    return this.getRandomNum() < totalChance;
	}
	
	public boolean isDriverPitting(int tyreUsage, boolean isTyreWrong, boolean isTyreFlat) {
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
	
	public boolean isTyreFlat(int tyreUsage) {
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
