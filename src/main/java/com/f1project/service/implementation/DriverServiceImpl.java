package com.f1project.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.f1project.exception.ResourceNotFoundException;
import com.f1project.mapper.CentralMapper;
import com.f1project.model.entity.Country;
import com.f1project.model.entity.Driver;
import com.f1project.model.entity.Team;
import com.f1project.model.request.DriverRequest;
import com.f1project.repository.DriverRepository;
import com.f1project.service.CountryService;
import com.f1project.service.DriverService;
import com.f1project.service.TeamService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DriverServiceImpl implements DriverService {
	
	private DriverRepository driverRepo;
	private CentralMapper mapper;
	private CountryService countryService;
	private TeamService teamService;

	@Override
	public List<Driver> findAllDrivers() {
		List<Driver> drivers = this.driverRepo.findAll();
		
		return drivers;
	}

	@Override
	public Driver findDriverById(Long id) {
		return this.driverRepo.findById(id).orElseThrow(() -> {throw new ResourceNotFoundException("A driver with ID=" + id + " doesn't exists");});
	}

	@Override
	public Driver saveDriver(DriverRequest driverRequest) {
		Long nationalityId = driverRequest.getCountryId();
		Country country = this.countryService.findCountryById(nationalityId);
		
		Long teamId = driverRequest.getTeamId();
		Team team = this.teamService.findTeamById(teamId);
		
		Driver driver = mapper.request2Driver(driverRequest);
		driver.setNationality(country);
		driver.setTeam(team);
		
		Driver savedDriver = this.driverRepo.save(driver);
		
		return savedDriver;
	}

	@Override
	public void deleteDriver(Long id) {
		this.findDriverById(id);
		
		this.driverRepo.deleteById(id);
	}

	@Override
	public void deleteAllDrivers() {
		this.driverRepo.deleteAll();
	}

	@Override
	public Driver updateDriver(DriverRequest driverRequest) {
		this.findDriverById(driverRequest.getId());
		
		Long nationalityId = driverRequest.getCountryId();
		Country country = this.countryService.findCountryById(nationalityId);
		
		Long teamId = driverRequest.getTeamId();
		Team team = this.teamService.findTeamById(teamId);	
		
		Driver driver = mapper.request2Driver(driverRequest);
		driver.setId(driverRequest.getId());
		driver.setNationality(country);
		driver.setTeam(team);
		
		Driver savedDriver = this.driverRepo.save(driver);
		
		return savedDriver;
	}

	@Override
	public List<Driver> createPreMadeDrivers() {
		List<Driver> drivers = new ArrayList<>();
		
		List<List<String>> sDrivers = new ArrayList<>(List.of(
			    List.of("Lewis Hamilton", "Reino Unido", "Mercedes"),
			    List.of("Michael Schumacher", "Alemanha", "Ferrari"),
			    List.of("Ayrton Senna", "Brasil", "McLaren"),
			    List.of("Max Verstappen", "Países Baixos", "Red Bull"),
			    List.of("Juan Manuel Fangio", "Argentina", "Alfa Romeo"),
			    List.of("Alain Prost", "França", "McLaren"),
			    List.of("Sebastian Vettel", "Alemanha", "Red Bull"),
			    List.of("Fernando Alonso", "Espanha", "Renault"),
			    List.of("Niki Lauda", "Áustria", "Ferrari"),
			    List.of("Nelson Piquet", "Brasil", "Brabham"),
			    List.of("Jackie Stewart", "Reino Unido", "Tyrrell"),
			    List.of("Emerson Fittipaldi", "Brasil", "McLaren"),
			    List.of("Jim Clark", "Reino Unido", "Lotus"),
			    List.of("Mika Häkkinen", "Finlândia", "McLaren"),
			    List.of("Nigel Mansell", "Reino Unido", "Williams"),
			    List.of("Kimi Räikkönen", "Finlândia", "Ferrari"),
			    List.of("Jenson Button", "Reino Unido", "Brawn GP"),
			    List.of("Nico Rosberg", "Alemanha", "Mercedes"),
			    List.of("Jacques Villeneuve", "Canadá", "Williams"),
			    List.of("Damon Hill", "Reino Unido", "Williams"),
			    List.of("Graham Hill", "Reino Unido", "BRM"),
			    List.of("James Hunt", "Reino Unido", "McLaren"),
			    List.of("Mario Andretti", "Estados Unidos", "Lotus"),
			    List.of("Jack Brabham", "Austrália", "Brabham"),
			    List.of("Alberto Ascari", "Itália", "Ferrari"),
			    List.of("John Surtees", "Reino Unido", "Ferrari"),
			    List.of("Phil Hill", "Estados Unidos", "Ferrari"),
			    List.of("Alan Jones", "Austrália", "Williams"),
			    List.of("Keke Rosberg", "Finlândia", "Williams"),
			    List.of("Jody Scheckter", "República Sul-Africana", "Ferrari"),
			    List.of("Jochen Rindt", "Áustria", "Lotus"),
			    List.of("Denny Hulme", "Nova Zelândia", "Brabham"),
			    List.of("Mike Hawthorn", "Reino Unido", "Ferrari"),
			    List.of("Giuseppe Farina", "Itália", "Alfa Romeo"),
			    List.of("Lando Norris", "Reino Unido", "McLaren"),
			    List.of("Arthur", "Brasil", "Red Bull"),
			    List.of("Paulo", "Brasil", "Red Bull"),
			    List.of("Leonardo", "Brasil", "Red Bull"),
			    List.of("Marcelo", "Brasil", "Red Bull"),
			    List.of("Mathias", "Brasil", "Red Bull"),
			    List.of("Bino", "Brasil", "Red Bull"),
			    List.of("Nícolas", "Brasil", "Red Bull")
		));
		
		sDrivers.forEach((sDriver) -> {
			
			String name = sDriver.get(0);
			Country country = this.countryService.findCountryByBrazilian(sDriver.get(1));
			Team team = this.teamService.findTeamByName(sDriver.get(2));
			
			Driver driver = new Driver();
			driver.setName(sDriver.get(0));
			driver.setLevel(99);
			driver.setNationality(country);
			driver.setTeam(team);
			
			drivers.add(driver);
			
			this.driverRepo.save(driver);
		});
		
		return drivers;
	}

	@Override
	public List<Driver> findAllDriversOrderByNameAsc() {
		return this.driverRepo.findAllByOrderByNameAsc();
	}

	@Override
	public List<Driver> createClassDrivers() {
		List<Driver> drivers = new ArrayList<>();
		
		List<List<String>> sDrivers = new ArrayList<>(List.of(
				List.of("Arthur", "Brasil", "Red Bull"),
				List.of("Leonardo", "Brasil", "Red Bull"),
				List.of("Paulo", "Brasil", "Red Bull"),
				List.of("Mathias", "Brasil", "Red Bull"),
				List.of("Nícolas", "Brasil", "Red Bull"),
				List.of("Marcelo", "Brasil", "Red Bull"),
				List.of("Bino", "Brasil", "Red Bull"),
				List.of("Kelly", "Brasil", "Red Bull"),
				List.of("Joice", "Brasil", "Red Bull"),
				List.of("Boenny", "Brasil", "Red Bull"),
				List.of("Martins", "Brasil", "Red Bull"),
				List.of("Kunrath", "Brasil", "Red Bull"),
				List.of("Meurer", "Brasil", "Red Bull"),
				List.of("Caio", "Brasil", "Red Bull"),
				List.of("Kauã", "Brasil", "Red Bull"),
				List.of("Felipe", "Brasil", "Red Bull"),
				List.of("Augusto", "Brasil", "Red Bull"),
				List.of("Rafael", "Brasil", "Red Bull"),
				List.of("Luiz", "Brasil", "Red Bull"),
				List.of("Kich", "Brasil", "Red Bull"),
				List.of("Francesco", "Brasil", "Red Bull"),
				List.of("Thaila", "Brasil", "Red Bull"),
				List.of("Milena", "Brasil", "Red Bull"),
				List.of("Nauany", "Brasil", "Red Bull"),
				List.of("Davi", "Brasil", "Red Bull"),
				List.of("Luft", "Brasil", "Red Bull"),
				List.of("Garcia", "Brasil", "Red Bull")
		));
		
		sDrivers.forEach((sDriver) -> {
			
			String name = sDriver.get(0);
			Country country = this.countryService.findCountryByBrazilian(sDriver.get(1));
			Team team = this.teamService.findTeamByName(sDriver.get(2));
			
			Driver driver = new Driver();
			driver.setName(sDriver.get(0));
			driver.setLevel(99);
			driver.setNationality(country);
			driver.setTeam(team);
			
			drivers.add(driver);
			
			this.driverRepo.save(driver);
		});
		
		return drivers;
	}
}
