package com.f1project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.f1project.model.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {

}
