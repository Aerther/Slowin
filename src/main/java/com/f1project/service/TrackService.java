package com.f1project.service;

import java.util.List;

import com.f1project.model.dto.TrackDTO;
import com.f1project.model.entity.Track;
import com.f1project.request.TrackRequest;

public interface TrackService {
	List<Track> findAllTracks();
	
	Track findTrackById(Long id);
	
	Track saveTrack(TrackRequest trackRequest);
	
	Track updateTrack(TrackRequest trackRequest);
	
	void deleteTrack(Long id);
	
	void deleteAllTracks();
}
