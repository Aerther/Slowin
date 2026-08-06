package com.f1project.service;

import java.util.List;

import com.f1project.model.entity.Race;
import com.f1project.model.entity.RaceEvent;
import com.f1project.model.entity.RaceResult;
import com.f1project.model.enums.EventType;
import com.f1project.model.request.CountryRequest;

public interface RaceEventService {
	public List<RaceEvent> findAllRaceEventsByRaceId(Long raceId);
	
	public RaceEvent saveRaceEvent(Race race, EventType eventType, RaceResult raceResult, int lap, String message);
	
	void deleteRaceEvent(Long id);
	
	void deleteAllRaceEvents();
}
