package com.f1project.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LapCondition {
	private boolean isPitting;
	private boolean isTyreWrong;
	private boolean isTyreFlat;
	private boolean isRetiring;
	private boolean isSafetyCarOn;
}