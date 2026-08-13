package com.f1project.utils;

import java.util.Locale;

import com.f1project.model.entity.Driver;
import com.f1project.model.entity.RaceResult;
import com.f1project.model.enums.EventType;
import com.f1project.model.enums.Mistake;
import com.f1project.model.enums.WeatherCondition;

public class StringFormatter {
	public static String formatLapTime(double totalSeconds) {
	    if (Double.isNaN(totalSeconds) || totalSeconds < 0) {
	        return "00.000";
	    }

	    long totalMillis = Math.round(totalSeconds * 1000.0);

	    long hours = totalMillis / 3_600_000;
	    long minutes = (totalMillis % 3_600_000) / 60_000;
	    long seconds = (totalMillis % 60_000) / 1_000;
	    long millis = totalMillis % 1_000;
	    
	    if (hours > 0) {
	        return String.format(Locale.US, "%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
	    }
	    
	    if(minutes > 0) {
	    	return String.format(Locale.US, "%d:%02d.%03d", minutes, seconds, millis);
	    }
	    
	    return String.format(Locale.US, "%02d.%03d", seconds, millis);
	}
	
	public static String formatFastestLap(RaceResult raceResult, LapTimeBreakDown breakdown) {
		String format = "Piloto %s fez a volta mais rápida (%s)";
		
		String driverName = raceResult.getDriver().getName();
		String fastestLap = raceResult.getFastestLapTime();
		
		return String.format(format, driverName, fastestLap);
	}
	
	public static String formatPitStop(RaceResult raceResult, LapTimeBreakDown breakdown) {
		String format = "Piloto %s parou nos boxes (parada de %.1fs) e trocou para o pneu %s";
		
		String driverName = raceResult.getDriver().getName();
		double pitStopTimeLoss = breakdown.getPitStopLoss();
		String tyreName = raceResult.getTyre().getDescription();
		
		return String.format(format, driverName, pitStopTimeLoss, tyreName);
	}
	
	public static String formatDriverMistake(RaceResult raceResult, Mistake mistake) {
		String format = "Piloto %s cometeu um erro %s";
		
		String driverName = raceResult.getDriver().getName();
		String mistakeDescription = mistake.getDescriptionBrazilian();
		
		return String.format(format, driverName, mistakeDescription);
	}
	
	public static String formatFanInvasion(Driver driverWhoHitFan) {
		String format = "Um fã invadiu a corrida e foi atropelado por %s";
		
		String driverName = driverWhoHitFan.getName();
		
		return String.format(format, driverName);
	}
	
	public static String formatWeatherChanged(WeatherCondition weatherCondition) {
		String format = "Clima mudou para %s";
		
		String weatherName = weatherCondition.getDescription();
		
		return String.format(format, weatherName);
	}
	
	public static String formatLeadChanged(RaceResult raceResult) {
		String format = "Piloto %s assumiu a primeira posição";
		
		String driverName = raceResult.getDriver().getName();
		
		return String.format(format, driverName);
	}
	
	public static String formatRetirement(RaceResult raceResult) {
		String format = "Piloto %s abandonou a corrida";
		
		String driverName = raceResult.getDriver().getName();
		
		return String.format(format, driverName);
	}
	
	public static String formatRaceStatusChanged() {
		String format = "Status da Corrida mudou";
		
		return String.format(format);
	}
}
