package com.f1project.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
    PIT_STOP("PITSTOP"),
    DRIVER_MISTAKE("DRIVERMISTAKE"),
    FAN_INVASION("FANINVASION"),
    WEATHER_CHANGED("WEATHERCHANGED"),
    LEAD_CHANGE("LEADCHANGE"),
    RETIREMENT("RETIREMENT"),
    RACE_STATUS_CHANGED("RACESTATUSCHANGED");
	
	private final String description;
}
