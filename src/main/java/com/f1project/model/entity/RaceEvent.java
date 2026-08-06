package com.f1project.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.f1project.model.enums.EventType;
import com.f1project.model.enums.RaceStatus;
import com.f1project.utils.RaceRules;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="race_event")
public class RaceEvent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int lap;
	
	private EventType eventType;
	
	@ManyToOne
	@JoinColumn(name = "race_id")
	private Race race;

	@ManyToOne
	@JoinColumn(name = "race_result_id")
	private RaceResult raceResult;
	
	private String message;
	
	private LocalDateTime createdAt;
}
