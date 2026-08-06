package com.f1project.simulation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.f1project.model.entity.Team;
import com.f1project.model.enums.Mistake;
import com.f1project.model.enums.RaceStatus;
import com.f1project.model.enums.Tyre;
import com.f1project.model.enums.WeatherCondition;
import com.f1project.utils.LapCondition;

@Service
public class SimulationCalculator {
	private Random random = new Random();
	
	public int getDurationOfSafetyCarInLaps(RaceStatus raceStatus) {
		if(raceStatus == RaceStatus.VSC) return this.randomBetweenInteger(1, 3);
		
		if(raceStatus == RaceStatus.SAFETYCAR) return this.randomBetweenInteger(3, 6);
		
		return 0;
	}
	
	public double calculateTyreTypeTime(Tyre tyre, LapCondition lapCondition) {
		if(lapCondition.isTyreWrong() || lapCondition.isTyreFlat()) return 0;
		
		return this.randomBetween(1.7, 2.3) * (1 - tyre.getEfficiency());
	}
	
	public int useTyre(RaceStatus raceStatus, Tyre tyre, Team team) {
	    int baseDegradation = this.randomBetweenInteger(tyre.getDegradationLostDownBound(), tyre.getDegradationLostUpperBound());
	    
	    if (raceStatus.isSafety()) {
	        baseDegradation = baseDegradation - 1;
	    }
	    
	    baseDegradation = Math.max(1, baseDegradation);
	    
	    double tyreWearFactor = (team != null) ? team.getTyreWearFactor() : 1.0;
	    
	    int finalDegradation = (int) Math.round(baseDegradation * tyreWearFactor);
	    
	    return Math.max(1, finalDegradation);
	}
	
	public double calculateDriverAndEngineVariation(RaceStatus raceStatus, int driverLevel, Team team) {
	    int motorForce = (team != null) ? team.getMotorForce() : 50;
	    
	    double driverImpact = driverLevel / 50.0;
	    
	    double engineImpact = motorForce / 50.0;
	    
	    double combinedPerformance = (driverImpact * 0.5) + (engineImpact * 0.5);
	    
	    return (-1.0) * combinedPerformance;
	}
	
	public double calculateLapVariation(int driverLevel) {
	    double maxVariation = 0.80 - (driverLevel / 100.0) * 0.50;
	    
	    return this.randomBetween(-maxVariation, maxVariation);
	}
	
	public double calculateTyreUsageTimeLoss(int tyreUsage) {
		return ((-1) * (tyreUsage) * ( 1.0/25 ) + 4);
	}
	
	public double calculateDriverMistake(RaceStatus raceStatus, LapCondition lapCondition, int driverLevel) {
		double extraLapTime = 0;
		double multiplier = 1 - (driverLevel) / 200.0;
		
		if(raceStatus.isSafety()) {
			multiplier = multiplier * 0.05;
		}
		
		if(lapCondition.isTyreWrong()) {
			multiplier = multiplier * 2;
		}
		
		List<Mistake> mistakes = Mistake.getMistakesValues();
		
		for(Mistake mistake : mistakes) {
			if(!SimulationStatus.didDriverMadeMistake(mistake, multiplier)) continue;
			
			extraLapTime += this.randomBetween(mistake.getTimeLostDownBound(), mistake.getTimeLostUpperBound());
		}
		
		return extraLapTime;
	}
	
	public double calculateSafetyCarOnTime() {
		return 6 + this.randomBetween(0.5, 1);
	}
	
	public double calculatePitStopTime(RaceStatus raceStatus, Team team) {
	    int pitEfficiency = (team != null) ? team.getPitStopEfficiency() : 50;

	    double basePitTime = 20.0 + ((100 - pitEfficiency) / 50.0);

	    double maxRandomTime = 4.0 - (pitEfficiency / 100.0) * 2.0;
	    double pitStopVariation = this.randomBetween(1.0, maxRandomTime);

	    double totalPitTime = basePitTime + pitStopVariation;

	    return totalPitTime * raceStatus.getTimeLossMultiplier();
	}
	
	public double calculateTyreWrongTime(RaceStatus raceStatus) {
		return 5 + this.randomBetween(1, 5);
	}
	
	public double calculateTyreFlatTime(RaceStatus raceStatus) {
		return (7 + this.randomBetween(0, 5)) * raceStatus.getTimeLossMultiplier();
	}
	
	private double randomBetween(double min, double max) {
	    return min + random.nextDouble() * (max - min);
	}
	
	public int randomBetweenInteger(int min, int max) {
		return min + random.nextInt(max - min + 1);
	}
}
