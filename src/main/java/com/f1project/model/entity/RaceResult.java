package com.f1project.model.entity;

import com.f1project.utils.enums.DriverStatus;
import com.f1project.utils.enums.Tyre;

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
@Table(name="race_result")
public class RaceResult {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	private double raceTime = 0;
	private double fastestLap = 100000;
	private double currentLap;
	private double difference = 0;
	private double differenceToFirst = 0;
	
	private String differenceToFirstTime;
	private String differenceTime;
	private String fastestLapTime;
	private String currentLapTime;
	private String totalRaceTime;
	
	private int lapRetired = 0;
	private DriverStatus driverStatus = DriverStatus.RACING;
	private Tyre tyre = Tyre.SOFT;
	private int tyreUsage = 100;
	private int stint = 0;
	
	private int position = 1;
	private int lastPosition = 1;
	private int pitStopQuantity = 0;
	
	@ManyToOne
	private Driver driver;
	
	@ManyToOne
	@JoinColumn(name = "race_id")
	private Race race;
}
