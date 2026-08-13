package com.f1project.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
    PIT_STOP("PITSTOP"),
    DRIVER_MISTAKE("DRIVER MISTAKE"),
    FAN_INVASION("FAN INVASION"),
    WEATHER_CHANGED("WEATHER CHANGED"),
    LEAD_CHANGE("LEAD CHANGE"),
    RETIREMENT("RETIREMENT"),
    RACE_STATUS_CHANGED("RACE STATUS CHANGED"),
    FASTEST_LAP("FASTEST LAP");
	
	private final String description;
}
