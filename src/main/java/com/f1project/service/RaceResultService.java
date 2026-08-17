package com.f1project.service;

import java.util.List;

import com.f1project.model.dto.RaceResultDTO;
import com.f1project.model.entity.RaceResult;

public interface RaceResultService {
	RaceResult findRaceResultById(Long id);
	List<RaceResult> findAllRaceResultsByRaceId(Long raceId);
	
	RaceResult saveRaceResult(RaceResultDTO raceResultDTO);
	RaceResult updateRaceResult(RaceResultDTO raceResultDTO);
	
	void deleteRaceResultById(Long id);
	void deleteAllRaceResultsByRaceId(Long raceId);
	void deleteAllRaceResults();
}