package com.f1project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.f1project.model.entity.RaceResult;

public interface RaceResultRepository extends JpaRepository<RaceResult, Long> {
	public Optional<RaceResult> findByRaceId(Long raceId);
}
