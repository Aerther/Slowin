package com.f1project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.f1project.model.entity.Track;

public interface TrackRepository extends JpaRepository<Track, Long> {

}
