package com.f1project.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.f1project.mapper.CentralMapper;
import com.f1project.model.dto.CountryDTO;
import com.f1project.model.dto.DriverDTO;
import com.f1project.model.dto.TeamDTO;
import com.f1project.model.request.DriverRequest;
import com.f1project.service.CountryService;
import com.f1project.service.DriverService;
import com.f1project.service.TeamService;
import com.f1project.utils.SortList;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/drivers")
public class DriverController {
	
	private DriverService driverService;
	private CountryService countryService;
	private TeamService teamService;
	private CentralMapper mapper;
	
	// CREATE OPERATION
	
	@GetMapping("/create")
	public String showDriverCreationForm(Model model) {
		List<CountryDTO> countries = mapper.countries2DTOList(this.countryService.findAllCountriesOrderByBrazilianAsc());
		List<TeamDTO> teams = mapper.teams2DTOList(this.teamService.findAllTeamsOrderByNameAsc());
		
		model.addAttribute("countries", countries);
		model.addAttribute("teams", teams);
		
		model.addAttribute("activePage", "drivers");
		
		return "drivers/create";
	}
	
	@PostMapping("/create")
	public String createDriver(DriverRequest driverRequest) {
		this.driverService.saveDriver(driverRequest);
		
		return "redirect:/drivers";
	}
	
	// UPDATE OPERATION
	
	@GetMapping("/update/{driverId}")
	public String showDriverEditForm(@PathVariable("driverId") Long driverId, Model model) {
		DriverDTO driver = mapper.driver2DTO(this.driverService.findDriverById(driverId));
		
		List<CountryDTO> countries = mapper.countries2DTOList(this.countryService.findAllCountriesOrderByBrazilianAsc());
		List<TeamDTO> teams = mapper.teams2DTOList(this.teamService.findAllTeamsOrderByNameAsc());
		
		model.addAttribute("countries", countries);
		model.addAttribute("teams", teams);
		model.addAttribute("driver", driver);
		
		model.addAttribute("activePage", "drivers");
		
		return "drivers/update";
	}
	
	@PostMapping("/update")
	public String updateDriver(DriverRequest driverRequest) {
		this.driverService.updateDriver(driverRequest);
		
		return "redirect:/drivers";
	}
	
	// DELETE OPERATION
	
	@GetMapping("/delete")
	public String deleteDriver(@RequestParam("driverId") Long driverId) {
		this.driverService.deleteDriver(driverId);
		
		return "redirect:/drivers";
	}
	
	// SHOW OPERATION
	
	@GetMapping
	public String showDrivers(Model model) {
		List<DriverDTO> drivers = mapper.drivers2DTOList(this.driverService.findAllDriversOrderByNameAsc());
		
		model.addAttribute("drivers", drivers);
		model.addAttribute("activePage", "drivers");
		
		return "drivers/list";
	}
}
