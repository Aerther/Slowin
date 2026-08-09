package com.f1project.utils;

import java.util.List;

import com.f1project.model.enums.Mistake;

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
	private List<Mistake> mistakesDone;
}