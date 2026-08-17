package com.f1project.controller.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.f1project.mapper.CentralMapper;
import com.f1project.model.request.TeamRequest;
import com.f1project.service.TeamService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/teams")
@ConditionalOnProperty(name = "app.enable-test-endpoints", havingValue = "true")
public class TeamTestController {
	private TeamService teamService;
	private CentralMapper mapper;
	
	@GetMapping("/delete/all")
	public String deleteAllTeams() {
		this.teamService.deleteAllTeams();
		
		return "redirect:/teams";
	}
	
	// DELETE OPERATION
	
	@GetMapping("/delete")
	public String deleteTeam(@RequestParam("teamId") Long teamId) {
		this.teamService.deleteTeam(teamId);
			
		return "redirect:/teams";
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
}
