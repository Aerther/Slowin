package com.f1project.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackRequest {
	private Long id;
	
	private String name;
	private String city;
	
	private double fastestTime;
	private double length;
	private double latitude;
	private double longitude;
	
	private Long countryId;
}
