package com.f1project.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.f1project.mapper.CentralMapper;
import com.f1project.model.entity.Race;
import com.f1project.model.entity.RaceEvent;
import com.f1project.model.entity.RaceResult;
import com.f1project.model.enums.EventType;
import com.f1project.repository.DriverRepository;
import com.f1project.repository.RaceEventRepository;
import com.f1project.service.CountryService;
import com.f1project.service.RaceEventService;
import com.f1project.service.TeamService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RaceEventServiceImpl implements RaceEventService {
	
	private RaceEventRepository raceEventRepo;

	@Override
	public List<RaceEvent> findAllRaceEventsByRaceId(Long raceId) {
		return this.raceEventRepo.findRaceEventsByRaceId(raceId);
	}

	@Override
	public RaceEvent saveRaceEvent(Race race, EventType eventType, RaceResult raceResult, int lap, String message) {
		RaceEvent raceEvent = new RaceEvent(null, race, eventType, raceResult, lap, message);
		
		RaceEvent savedRaceEvent = this.raceEventRepo.save(raceEvent);
		
		return savedRaceEvent;
	}
	
	@Override
	public List<RaceEvent> saveAllRaceEvents(List<RaceEvent> raceEvents) {
		List<RaceEvent> savedRaceEvents = this.raceEventRepo.saveAll(raceEvents);
		
		return savedRaceEvents;
	}

	@Override
	public void deleteRaceEvent(Long id) {
		
	}

	@Override
	public void deleteAllRaceEvents() {
		
	}

}
