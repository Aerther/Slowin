package com.f1project.helper;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.f1project.helper.enums.Mistake;
import com.f1project.helper.enums.RaceStatus;
import com.f1project.helper.enums.Tyre;
import com.f1project.helper.enums.WeatherCondition;
import com.f1project.model.entity.Driver;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class Simulation {
	
	private Random random = new Random();
	
	private double randomBetween(double min, double max) {
	    return min + random.nextDouble() * (max - min);
	}
	
	private double getRandomNum() {
		return random.nextDouble() * 100;
	}
	
	public double calculateDriverLevelVariation(RaceStatus raceStatus, int driverLevel) {
		return (-1.0) * driverLevel / 50;
	}
	
	public double calculateLapVariation() {
		return this.randomBetween(-0.3, 0.3);
	}
	
	public double calculateTyreUsage(int tyreUsage) {
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
		
		Mistake small = Mistake.SMALL;
		Mistake medium = Mistake.MEDIUM;
		Mistake big = Mistake.BIG;
		
		if(this.getRandomNum() < small.getChance() * multiplier) {
			extraLapTime += this.randomBetween(small.getTimeLostDownBound(), small.getTimeLostUpperBound());
		}
		
		if(this.getRandomNum() < medium.getChance() * multiplier) {
			extraLapTime += this.randomBetween(medium.getTimeLostDownBound(), medium.getTimeLostUpperBound());
		}
		
		if(this.getRandomNum() < big.getChance() * multiplier) {
			extraLapTime += this.randomBetween(big.getTimeLostDownBound(), big.getTimeLostUpperBound());
		}
		
		return extraLapTime;
	}
	
	public double calculateSafetyCarOnTime() {
		return 6 + this.randomBetween(0.5, 1);
	}
	
	public double calculatePitStopTime(RaceStatus raceStatus) {
		return (20 + this.randomBetween(1, 4)) * raceStatus.getTimeLossMultiplier();
	}
	
	public double calculateTyreWrongTime(RaceStatus raceStatus) {
		return 5 + this.randomBetween(1, 5);
	}
	
	public double calculateTyreFlatTime(RaceStatus raceStatus) {
		return (7 + this.randomBetween(0, 5)) * raceStatus.getTimeLossMultiplier();
	}
	
	public boolean isSafetyCarOn(RaceStatus raceStatus) {
		return raceStatus.isSafety();
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
	
	public boolean isTyreWrong(WeatherCondition weatherCondition, Tyre tyre) {
		return weatherCondition.isDry() != tyre.isDryTyre(); 
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
	
	public boolean isDriverRetiring(boolean isTyreWrong) {
		double multiplier = 1;
		
		if(isTyreWrong) multiplier = 2;
		
		return this.getRandomNum() < 0.07 * multiplier;
	}
	
	public boolean isSafetyCarComing() {
		return this.getRandomNum() < 60;
	}
	
	public int useTyre(RaceStatus raceStatus, Tyre tyre) {
		int degradation = this.randomBetweenInteger(tyre.getDegradationLostDownBound(), tyre.getDegradationLostUpperBound());
		
		if(raceStatus.isSafety()) {
			degradation = degradation - 1;
		}
		
		degradation = Math.max(1, degradation);
		
		return degradation;
	}
	
	public int randomBetweenInteger(int min, int max) {
		return min + random.nextInt() * (max - min);
	}
}
