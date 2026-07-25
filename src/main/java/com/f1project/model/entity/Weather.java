package com.f1project.model.entity;

import com.f1project.helper.enums.WeatherCondition;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
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
