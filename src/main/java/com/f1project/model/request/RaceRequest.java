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
    
    @JsonProperty("allow_retirement")
    private boolean driverRetirementEnabled = true;
    
    @JsonProperty("allow_flat_tyre")
    private boolean flatTyreEnabled = true;
    
    @JsonProperty("allow_fan_invasion")
    private boolean fanInvasionEnabled = true;
    
    @JsonProperty("allow_safety_car")
    private boolean safetyCarEnabled = true;
    
    @JsonProperty("allow_weather_change")
    private boolean weatherChangeEnabled = true;
    
    @JsonProperty("allow_driver_pitting")
    private boolean driverPittingEnabled = true;
    
    @JsonProperty("allow_driver_tyre_wear")
    private boolean driverTyreWearEnabled = true;
}
