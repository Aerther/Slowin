package com.f1project.helper;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.f1project.helper.enums.Mistake;
import com.f1project.helper.enums.Tyre;
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
	
	public double calculateDriverLevelVariation(int driverLevel) {
		return (-1.0) * driverLevel / 50;
	}
	
	public double calculateLapVariation() {
		return this.randomBetween(-0.3, 0.3);
	}
	
	public double calculateTyreUsage(int tyreUsage) {
		return ((-1) * (tyreUsage) * ( 1.0/25 ) + 4);
	}
	
	public double calculateDriverMistake(int driverLevel) {
		double extraLapTime = 0;
		double multiplier = 1 - (driverLevel) / 200.0;
		
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
	
	public double calculatePitStopTime() {
		return 22;
	}
	
	public boolean isDriverPitting(int tyreUsage) {
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
	
	public boolean isDriverRetiring() {
		return this.getRandomNum() < 0.07;
	}
	
	public int useTyre(Tyre tyre) {
		if(tyre == Tyre.SOFT) {
			return Tyre.SOFT.getTimeLostDownBound();
		}
		
		if(tyre == Tyre.MEDIUM) {
			return Tyre.MEDIUM.getTimeLostDownBound();
		}
		
		if(tyre == Tyre.HARD) {
			return Tyre.HARD.getTimeLostDownBound();
		}
		
		if(tyre == Tyre.INTERMEDIATE) {
			return Tyre.INTERMEDIATE.getTimeLostDownBound();
		}
		
		if(tyre == Tyre.WET) {
			return Tyre.WET.getTimeLostDownBound();
		}
		
		return 8;
	}
}
