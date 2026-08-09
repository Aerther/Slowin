package com.f1project.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Mistake {
	SMALL("Pequeno", 0.4, 2, 8),
	MEDIUM("Médio", 2, 5, 4),
	BIG("Grande", 5, 10, 1);
	
	private String descriptionBrazilian;
	private double timeLostDownBound;
	private double timeLostUpperBound;
	private double chance;
	
	public static List<Mistake> getMistakesValues() {
		return Arrays.stream(Mistake.values()).collect(Collectors.toList());
	}
}
