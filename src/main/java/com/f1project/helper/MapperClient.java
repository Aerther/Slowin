package com.f1project.helper;

import java.util.List;
import java.util.Map;

import com.f1project.client.response.CountryResponse;
import com.f1project.client.response.WeatherResponse;
import com.f1project.model.dto.CountryDTO;
import com.f1project.model.dto.WeatherDTO;

public class MapperClient {
	public static CountryDTO countryResponse2DTO(CountryResponse countryResponse) {
		String brazilian = countryResponse.getTranslations().get("pt");
		String flagUrlPng = countryResponse.getFlags().get("png");
		
		CountryDTO countryDTO = new CountryDTO(
			null,
			countryResponse.getName(),
			countryResponse.getCapital(),
			countryResponse.getCode(),
			flagUrlPng,
			brazilian
		);
		
		return countryDTO;
	}
	
	public static WeatherDTO weatherResponse2DTO(WeatherResponse weatherResponse) {
		if(weatherResponse == null) return null;
		
		Map<String, Object> currentWeather = weatherResponse.getCurrentWeather();
		currentWeather.forEach((key, value) -> {
		    System.out.println(key + " = " + value);
		});
		
		int code = (int) currentWeather.get("weather_code");
		double elevation = (double) weatherResponse.getElevation();
		double temperature = (double) currentWeather.get("temperature_2m");
		double precipitation = (double) currentWeather.get("rain");
		double windSpeed = (double) currentWeather.get("wind_speed_10m");
		
		WeatherDTO weatherDTO = new WeatherDTO(
			code,
			elevation,
			temperature,
			precipitation,
			windSpeed
		);
		
		return weatherDTO;
	}
}
