package com.f1project.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.f1project.mapper.CentralMapper;
import com.f1project.model.dto.TeamDTO;
import com.f1project.model.request.TeamRequest;
import com.f1project.service.TeamService;
import com.f1project.utils.SortList;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/teams")
public class TeamController {
	
	private TeamService teamService;
	private CentralMapper mapper;
	
	// SHOW OPERATION
	
	@GetMapping
	public String showTeams(Model model) {
		List<TeamDTO> teams = mapper.teams2DTOList(this.teamService.findAllTeamsOrderByNameAsc());
		
		model.addAttribute("teams", teams);
		model.addAttribute("activePage", "teams");
		
		return "teams/list";
	}
}
