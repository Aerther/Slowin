package com.f1project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {
	private Long id;
	
	private String name;
	
	private int motorForce;
	private int motorReliability;
	private double tyreWearFactor;
	private int pitStopEfficiency;
	
	private String primaryColor;
    private String logoUrl;  
}
