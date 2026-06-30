package com.f1project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDTO {
	private int code;
	private double elevation;
	private double temperature;
	private double precipitation;
	private double windSpeed;
}