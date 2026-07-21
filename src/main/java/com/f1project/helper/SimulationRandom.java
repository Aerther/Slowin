package com.f1project.helper;

import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class SimulationRandom {
	
	private Random random = new Random();
	
	private double randomBetween(double min, double max) {
	    return min + random.nextDouble() * (max - min);
	}
	
	private boolean randomPercentageChance(double percentage) {
		return random.nextDouble() * 100 < percentage;
	}
	
	public double calculateDriverLevelVariation(int driverLevel) {
		return (-1.0) * driverLevel * 2;
	}
	
	public double calculateLapVariation() {
		return this.randomBetween(-0.3, 0.3);
	}
	
	public double calculateDriverMistake(int driverLevel) {
		double extraLapTime = 0;
		double multiplier = 1 - (driverLevel) / 200.0;
		
		// Small mistake
		if(this.randomPercentageChance(5 * multiplier)) {
			extraLapTime += this.randomBetween(0.4, 2);
		}
		
		// Medium mistake
		if(this.randomPercentageChance(2 * multiplier)) {
			extraLapTime += this.randomBetween(2, 5);
		}
		
		// Serious mistake
		if(this.randomPercentageChance(0.2 * multiplier)) {
			extraLapTime += this.randomBetween(5, 10);
		}
		
		return extraLapTime;
	}
}
