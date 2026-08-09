package com.f1project.utils;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Service
public class LapTimeBreakDown {
	private double baseTrackTime = 0;
    private double lapVariation = 0;
    private double driverEngineVariation = 0;
    private double driverMistakeLoss = 0;
    private double tyreUsageLoss = 0;
    private double tyreTypeTime = 0;
    private double tyreWrongLoss = 0;
    private double tyreFlatLoss = 0;
    private double pitStopLoss = 0;
    private double safetyCarLoss = 0;
    
    public double getTotalLapTime() {
    	return baseTrackTime + lapVariation + driverEngineVariation 
                + driverMistakeLoss + tyreUsageLoss + tyreTypeTime 
                + tyreWrongLoss + tyreFlatLoss + pitStopLoss 
                + safetyCarLoss;
    }
}
