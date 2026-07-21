package com.f1project.service;

import java.util.List;

import com.f1project.model.dto.RaceDTO;
import com.f1project.model.entity.Race;
import com.f1project.request.RaceRequest;

public interface RaceService {
	List<Race> findAllRaces();
	
	Race findRaceById(Long id);
	
	Race saveRace(RaceRequest raceRequest);
	Race updateRace(RaceRequest raceRequest);
	Race updateRace(Race race);
	
	void deleteRaceById(Long id);
	void deleteAllRaces();
}
