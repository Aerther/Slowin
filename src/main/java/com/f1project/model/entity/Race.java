package com.f1project.model.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.f1project.model.enums.RaceStatus;
import com.f1project.utils.RaceRules;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name="race")
public class Race {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private LocalDateTime dateCreated;
	private LocalDateTime dateStarted;
	
	@Embedded
	private RaceRules raceRules;
	
	private RaceStatus raceStatus = RaceStatus.CREATED;
	private int lapsSafetyCarDuration = 0;
	
	private double fastestLap = 0;
	private int lapsQuantity;
	private int lapsDone;
	
	@Embedded
	private Weather weather;
	
	@ManyToOne
	@JoinColumn(name = "track_id")
	private Track track;
	
	@OneToMany(mappedBy = "race", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RaceResult> results;
}
