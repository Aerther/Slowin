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
				new Team(null, "McLaren", 97, 96, 0.92, 98, "#FF8000", "https://upload.wikimedia.org/wikipedia/commons/9/9d/McLaren_Mastercard_F1.jpg"),
			    new Team(null, "Ferrari", 96, 94, 0.95, 96, "#E80020", "https://upload.wikimedia.org/wikipedia/pt/5/58/Logo_da_Scuderia_Ferrari_HP.png"),
			    new Team(null, "Red Bull", 95, 95, 0.90, 99, "#141C3A", "https://upload.wikimedia.org/wikipedia/pt/c/c6/Logotipo_da_Red_Bull_Racing.png"),
			    new Team(null, "Mercedes", 95, 95, 0.93, 95, "#27F4D2", "https://upload.wikimedia.org/wikipedia/commons/f/fc/Mercedes-AMG_Petronas_F1_Team_logo_%282026%29.svg"),
			    new Team(null, "Aston Martin", 90, 89, 0.98, 91, "#229971", "https://upload.wikimedia.org/wikipedia/commons/0/03/Aston_Martin_F1_Team_logo_2024.jpg"),
			    new Team(null, "Williams", 88, 87, 0.96, 92, "#64C4FF", "https://upload.wikimedia.org/wikipedia/commons/1/12/Atlassian_Williams_F1_Team_logo.svg"),
			    new Team(null, "Racing Bulls", 86, 85, 0.97, 89, "#6692FF", "https://upload.wikimedia.org/wikipedia/pt/a/a1/Logotipo_da_RB_F1_Team.png"),
			    new Team(null, "Alpine", 85, 83, 1.02, 86, "#0093CC", "https://upload.wikimedia.org/wikipedia/commons/4/4a/BWT_Alpine_F1_Team_Logo.png"),
			    new Team(null, "Audi", 84, 85, 1.00, 85, "#F50500", "https://upload.wikimedia.org/wikipedia/commons/0/03/Audif1.com_logo17_%28cropped%29.svg"),
			    new Team(null, "Haas", 83, 82, 1.05, 84, "#B6BABD", "https://upload.wikimedia.org/wikipedia/commons/1/18/TGR_Haas_F1_Team_Logo_%282026%29.svg"),
			    new Team(null, "Cadillac", 82, 82, 1.03, 83, "#111111", "https://upload.wikimedia.org/wikipedia/commons/d/d8/Cadillac_Formula_1_Team_logo.png"),
			    
			    new Team(null, "Alfa Romeo", 85, 84, 1.00, 85, "#900000", "https://upload.wikimedia.org/wikipedia/commons/d/d4/Alfa_Romeo_F1_Team_Stake_Logo.svg"),
			    new Team(null, "Brabham", 88, 85, 0.98, 88, "#002F6C", "https://static.wikia.nocookie.net/logopedia/images/0/07/Brabham3.gif/revision/latest?cb=20210216135056"),
			    new Team(null, "Lotus", 92, 88, 0.95, 90, "#004225", "https://upload.wikimedia.org/wikipedia/pt/a/a1/Lotus_F1_Team.png"),
			    new Team(null, "Tyrrell", 86, 84, 0.99, 86, "#001F5B", "https://upload.wikimedia.org/wikipedia/commons/5/5e/Tyrrell.svg"),
			    new Team(null, "Renault", 89, 87, 0.97, 88, "#FFF000", "https://upload.wikimedia.org/wikipedia/commons/1/18/RENAULT_F1.svg"),
			    new Team(null, "Brawn GP", 93, 92, 0.92, 94, "#BFFF00", "https://upload.wikimedia.org/wikipedia/commons/2/24/Brawn_GP_logo.svg"),
			    new Team(null, "BRM", 84, 80, 1.01, 82, "#003319", "https://static.wikia.nocookie.net/logopedia/images/b/bd/BritishRacingMotorsLogo.png/revision/latest?cb=20190312201603")
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
