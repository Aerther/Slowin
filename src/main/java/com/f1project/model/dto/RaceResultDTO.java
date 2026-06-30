package com.f1project.model.dto;

import com.f1project.model.entity.Driver;
import com.f1project.model.entity.Race;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceResultDTO {
	
	private Long id;
	
	private double fastestLap;
	private double currentLap;
	private int position;
	private int pitStopQuantity;
	
	private Driver driver;
	
	private Race race;
}
