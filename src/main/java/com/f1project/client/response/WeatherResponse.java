package com.f1project.client.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
	
	@JsonProperty("elevation")
	private double elevation;
	
	@JsonProperty("current")
	private Map<String, Object> currentWeather;
}
