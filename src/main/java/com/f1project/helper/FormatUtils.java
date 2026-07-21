package com.f1project.helper;

import java.util.Locale;

public class FormatUtils {
	public static String formatLapTime(double seconds) {
	    int minutes = (int) (seconds / 60);
	    double remainingSeconds = seconds % 60;

	    return String.format(Locale.US, "%d:%06.3f", minutes, remainingSeconds);
	}
}
