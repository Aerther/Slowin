package com.f1project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.f1project.model.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {
	public List<Driver> findAllByOrderByNameAsc();
}
