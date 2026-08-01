package com.f1project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.f1project.helper.CentralMapper;
import com.f1project.model.dto.TeamDTO;
import com.f1project.request.TeamRequest;
import com.f1project.service.TeamService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Controller
@RequestMapping("/teams")
public class TeamController {
	
	private TeamService teamService;
	private CentralMapper mapper;
	
	// SHOW OPERATION
	
	@GetMapping
	public String showTeams(Model model) {
		List<TeamDTO> teams = mapper.teams2DTOList(this.teamService.findAllTeams());
		
		model.addAttribute("teams", teams);
		model.addAttribute("activePage", "teams");
		
		return "teams/list";
	}
	
	// CREATE OPERATION
	
	@GetMapping("/create")
	public String showTeamCreationForm(Model model) {
		return "teams/create";
	}
	
	@PostMapping("/create")
	public String createTeam(TeamRequest teamRequest) {
		this.teamService.saveTeam(teamRequest);
		
		return "redirect:/teams";
	}
	
	// DELETE OPERATION
	
	@GetMapping("/delete")
	public String deleteTeam(@RequestParam("teamId") Long teamId) {
		this.teamService.deleteTeam(teamId);
		
		return "redirect:/teams";
	}
	
	@GetMapping("/delete/all")
	public String deleteAllTeams() {
		this.teamService.deleteAllTeams();
		
		return "redirect:/teams";
	}
	
}
