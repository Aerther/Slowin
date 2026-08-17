package com.f1project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.f1project.model.entity.Driver;
import com.f1project.model.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
	public Optional<Team> findByName(String name);
	public List<Team> findAllByOrderByNameAsc();
}
