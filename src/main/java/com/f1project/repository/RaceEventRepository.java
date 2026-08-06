package com.f1project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.f1project.model.entity.RaceEvent;

public interface RaceEventRepository extends JpaRepository<RaceEvent, Long> {
	public List<RaceEvent> findRaceEventsByRaceId(Long raceId);
}
