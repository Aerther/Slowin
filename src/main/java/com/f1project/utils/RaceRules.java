package com.f1project.utils;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RaceRules {
	
	private boolean driverRetirementEnabled = true;
    private boolean flatTyreEnabled = true;
    private boolean fanInvasionEnabled = true;
    private boolean safetyCarEnabled = true;
    private boolean weatherChangeEnabled = true;
    private boolean driverPittingEnabled = true;
    private boolean driverTyreWearEnabled = true;
	
}
