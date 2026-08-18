package com.f1project.model.request;

import java.util.List;

import com.f1project.model.entity.Weather;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaceRequest {
	private Long id;
	
	private String name;
	
	private int lapsQuantity;
	
	private Long trackId;
	
    private List<Long> driversId;
    
    private boolean driverRetirementEnabled = false;
    private boolean flatTyreEnabled = false;
    private boolean fanInvasionEnabled = false;
    private boolean safetyCarEnabled = false;
    private boolean weatherChangeEnabled = false;
    private boolean driverPittingEnabled = false;
    private boolean driverTyreWearEnabled = false;
}
