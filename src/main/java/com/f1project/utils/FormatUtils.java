package com.f1project.utils;

import java.util.Locale;

public class FormatUtils {
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
}
