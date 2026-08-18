package com.f1project.utils;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Data
public class RaceRules {
	
	private boolean driverRetirementEnabled = true;
    private boolean flatTyreEnabled = true;
    private boolean fanInvasionEnabled = true;
    private boolean safetyCarEnabled = true;
    private boolean weatherChangeEnabled = true;
    private boolean driverPittingEnabled = true;
    private boolean driverTyreWearEnabled = true;
	
}
