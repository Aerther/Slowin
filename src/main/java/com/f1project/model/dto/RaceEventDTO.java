package com.f1project.model.dto;

import java.time.LocalDateTime;

import com.f1project.model.entity.Race;
import com.f1project.model.entity.RaceResult;
import com.f1project.model.enums.EventType;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceEventDTO {
	private Long id;
	
	private int lap;
	
	private EventType eventType;
	
	private Race race;

	private RaceResult raceResult;
	
	private String message;
	
	private LocalDateTime createdAt;
}
