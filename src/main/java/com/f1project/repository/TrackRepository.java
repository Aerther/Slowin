package com.f1project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.f1project.model.entity.Driver;
import com.f1project.model.entity.Track;

public interface TrackRepository extends JpaRepository<Track, Long> {
	public List<Track> findAllByOrderByNameAsc();
}
