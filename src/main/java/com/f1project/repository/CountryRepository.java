package com.f1project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.f1project.model.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
	public Optional<Country> findByName(String name);
}
