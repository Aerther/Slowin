package com.f1project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackDTO {
	private Long id;
	
	private String name;
	private String city;
	
	private double fastestTime;
	private String lapTime;
	private double length;
	private double latitude;
	private double longitude;
	
	private CountryDTO country;
}
