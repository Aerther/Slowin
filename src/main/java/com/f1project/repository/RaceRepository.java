package com.f1project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.f1project.model.entity.Race;

public interface RaceRepository extends JpaRepository<Race, Long>{
	public List<Race> findAllByOrderByDateCreatedDesc();
}
