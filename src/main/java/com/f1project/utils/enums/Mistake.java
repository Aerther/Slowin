package com.f1project.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Mistake {
	SMALL(0.4, 2, 8),
	MEDIUM(2, 5, 4),
	BIG(5, 10, 1);
	
	private double timeLostDownBound;
	private double timeLostUpperBound;
	private double chance;
}
