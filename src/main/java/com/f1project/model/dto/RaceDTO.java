package com.f1project.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.f1project.model.entity.RaceResult;
import com.f1project.model.entity.Track;
import com.f1project.model.entity.Weather;
import com.f1project.utils.enums.RaceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceDTO {
	private Long id;
	
	private String name;
	
	private LocalDateTime dateCreated;
	private LocalDateTime dateStarted;
	
	private RaceStatus raceStatus;
	private int lapsSafetyCarDuration;
	
	private double fastestLap;
	private int lapsQuantity;
	private int lapsDone;
	
	private WeatherDTO weather;

	private TrackDTO track;

    private List<RaceResultDTO> results;
}
