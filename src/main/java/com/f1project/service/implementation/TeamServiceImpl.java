package com.f1project.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.f1project.exception.ResourceNotFoundException;
import com.f1project.model.entity.Team;
import com.f1project.repository.TeamRepository;
import com.f1project.request.TeamRequest;
import com.f1project.service.TeamService;
import com.f1project.utils.mapper.CentralMapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {
	
	private CentralMapper mapper;
	private TeamRepository teamRepo;
	
	@Override
	public List<Team> findAllTeams() {
		List<Team> teams = this.teamRepo.findAll();
		
		return teams;
	}

	@Override
	public Team findTeamById(Long id) {
		return this.teamRepo.findById(id).orElseThrow(() -> {throw new ResourceNotFoundException("A TEAM with ID=" + id + " doesn't exists");});
	}

	@Override
	public Team findTeamByName(String name) {
		return this.teamRepo.findByName(name).orElseThrow(() -> {throw new ResourceNotFoundException("A TEAM with NAME=" + name + " doesn't exists");});
	}

	@Override
	public Team saveTeam(TeamRequest teamRequest) {
		Team team = mapper.request2Team(teamRequest);
		
		Team savedTeam = this.teamRepo.save(team);
		
		return savedTeam;
	}

	@Override
	public List<Team> createPreMadeTeams() {
		List<Team> teams = new ArrayList<>(
			List.of(
				new Team(null, "Red Bull", 100, 100, 1.0, 100, "#000000", ""),
				new Team(null, "Ferrari", 100, 100, 1.0, 100, "#000000", "")
			)	
		);
		
		List<Team> savedTeams = this.teamRepo.saveAll(teams);
		
		return savedTeams;
	}

	@Override
	public Team updateTeam(TeamRequest teamRequest) {
		return null;
	}

	@Override
	public void deleteTeam(Long id) {
		this.teamRepo.findById(id);
		
		this.teamRepo.deleteById(id);
	}

	@Override
	public void deleteAllTeams() {
		this.teamRepo.deleteAll();
	}

}
