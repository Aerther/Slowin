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
	
	private double raceTime;
	private double fastestLap;
	private double currentLap;
	private double difference;
	private double differenceToFirst;
	
	private String differenceToFirstTime;
	private String differenceTime;
	private String fastestLapTime;
	private String currentLapTime;
	private String totalRaceTime;
	
	private int position;
	private int pitStopQuantity;
	
	private DriverDTO driverDTO;
	
	private RaceDTO raceDTO;
}
