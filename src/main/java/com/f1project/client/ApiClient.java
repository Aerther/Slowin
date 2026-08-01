package com.f1project.client;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.f1project.client.response.CountryResponse;
import com.f1project.client.response.WeatherResponse;
import com.f1project.model.dto.CountryDTO;
import com.f1project.model.dto.WeatherDTO;
import com.f1project.utils.mapper.MapperClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class ApiClient {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public List<CountryDTO> getRestCountries() {
		String url = "https://www.apicountries.com/countries";
		
		CountryResponse[] countriesResponse = this.restTemplate.getForObject(url, CountryResponse[].class);
		
		return Arrays.stream(countriesResponse).filter(c -> c.isIndependent()).map(MapperClient::countryResponse2DTO).collect(Collectors.toList());
	}
	
	public WeatherDTO getWeatherCity(Double latitude, Double longitude) {
		String format = "https://api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&current=temperature_2m,rain,precipitation,weather_code,wind_speed_10m&forecast_days=1";
		String url = String.format(format, latitude, longitude);
		
		System.out.println("WEATHER URL: " + url);
		
		WeatherResponse weatherResponse = this.restTemplate.getForObject(url, WeatherResponse[].class)[0];
		
		return MapperClient.weatherResponse2DTO(weatherResponse);
	}
}
