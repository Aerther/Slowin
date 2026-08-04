package com.f1project.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RaceStatus {
	FINISHED("FINISHED", false, 1),
	ONGOING("ONGOING", false, 1),
	CREATED("CREATED", false, 1),
	SAFETYCAR("SAFETYCAR", true, 0.5),
	VSC("VSC", true, 0.5);
	
	private final String description;
	private final boolean isSafety;
	private final double timeLossMultiplier;
	
	public static RaceStatus getRandomSafetyStatus(RaceStatus raceStatus) {
		if(raceStatus == RaceStatus.SAFETYCAR) return RaceStatus.SAFETYCAR;
		
		Random random = new Random();
		
		List<RaceStatus> status = Arrays.stream(RaceStatus.values()).filter(rStatus -> rStatus.isSafety).collect(Collectors.toList());
		
		int rNum = random.nextInt(status.size());
		
		return status.get(rNum);
	}
}
