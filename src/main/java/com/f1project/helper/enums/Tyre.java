package com.f1project.helper.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Tyre {
	SOFT("Macio", true, 7, 7, "soft.png"),
	MEDIUM("Médio", true, 5, 5, "medium.png"),
	HARD("Duro", true, 3, 3, "hard.png"),
	INTERMEDIATE("Intermediário", false, 5, 5, "intermediate.png"),
	WET("Chuva", false, 4, 4, "wet.png");
	
	private String description;
	private boolean isDryTyre;
	private int timeLostDownBound;
	private int timeLostUpperBound;
	private String imageName;
	
	public String getTyreUrl() {
		return "/images/tyres/" + this.imageName;
	}
	
	public static Tyre chooseRandomTyre(WeatherCondition weatherCondition) {
		Random random = new Random();
		
		List<Tyre> tyres = Arrays.stream(Tyre.values()).filter(tyre -> tyre.isDryTyre == weatherCondition.isDry()).collect(Collectors.toList());
		
		int rNum = random.nextInt(tyres.size());
		
		return tyres.get(rNum);
	}
}
