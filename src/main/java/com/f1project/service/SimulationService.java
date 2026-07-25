package com.f1project.service;

import com.f1project.model.entity.Race;

public interface SimulationService {
	public Race simulateRace(Long raceId, int laps);
	public Race finishRace(Long raceId);
}
