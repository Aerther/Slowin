package com.f1project.model.dto;

import com.f1project.helper.enums.WeatherCondition;

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
	private WeatherCondition weatherCondition;
	
	public void setWeatherCondition() {
		this.weatherCondition = WeatherCondition.fromCode(code);
	}
}