package com.f1project.service;

import java.util.List;

import com.f1project.model.entity.Country;
import com.f1project.model.entity.Team;
import com.f1project.model.request.CountryRequest;
import com.f1project.model.request.TeamRequest;

public interface TeamService {
	public List<Team> findAllTeams();
	
	public Team findTeamById(Long id);
	
	public Team findTeamByName(String name);
	
	public Team saveTeam(TeamRequest teamRequest);
	
	public List<Team> createPreMadeTeams();
	
	public Team updateTeam(TeamRequest teamRequest);
	
	void deleteTeam(Long id);
	
	void deleteAllTeams();
}
