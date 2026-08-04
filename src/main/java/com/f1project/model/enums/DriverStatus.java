package com.f1project.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DriverStatus {
	PITTED("PITTED"),
	RACING("RACING"),
	RETIRED("DNF");
	
	private String description;
}
