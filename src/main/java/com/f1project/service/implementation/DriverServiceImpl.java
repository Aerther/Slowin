package com.f1project.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f1project.exception.ResourceNotFoundException;
import com.f1project.helper.CentralMapper;
import com.f1project.model.dto.CountryDTO;
import com.f1project.model.dto.DriverDTO;
import com.f1project.model.entity.Country;
import com.f1project.model.entity.Driver;
import com.f1project.repository.CountryRepository;
import com.f1project.repository.DriverRepository;
import com.f1project.request.DriverRequest;
import com.f1project.service.CountryService;
import com.f1project.service.DriverService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DriverServiceImpl implements DriverService {
	
	private DriverRepository driverRepo;
	private CentralMapper mapper;
	private CountryService countryService;

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
		
		Driver driver = mapper.request2Driver(driverRequest);
		driver.setNationality(country);
		
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
		
		Country country = this.countryService.findCountryById(driverRequest.getCountryId());		
		
		Driver driver = mapper.request2Driver(driverRequest);
		driver.setId(driverRequest.getId());
		driver.setNationality(country);
		
		Driver savedDriver = this.driverRepo.save(driver);
		
		return savedDriver;
	}

	@Override
	public List<Driver> createPreMadeDrivers() {
		List<Driver> drivers = new ArrayList<>();
		
		List<String> names = new ArrayList<>(List.of(
			    "Lewis Hamilton",
			    "Michael Schumacher",
			    "Ayrton Senna",
			    "Max Verstappen",
			    "Juan Manuel Fangio",
			    "Alain Prost",
			    "Sebastian Vettel",
			    "Fernando Alonso",
			    "Niki Lauda",
			    "Nelson Piquet",
			    "Jackie Stewart",
			    "Emerson Fittipaldi",
			    "Jim Clark",
			    "Mika Häkkinen",
			    "Nigel Mansell",
			    "Kimi Räikkönen",
			    "Jenson Button",
			    "Nico Rosberg",
			    "Jacques Villeneuve",
			    "Damon Hill",
			    "Graham Hill",
			    "James Hunt",
			    "Mario Andretti",
			    "Jack Brabham",
			    "Alberto Ascari",
			    "John Surtees",
			    "Phil Hill",
			    "Alan Jones",
			    "Keke Rosberg",
			    "Jody Scheckter",
			    "Jochen Rindt",
			    "Denny Hulme",
			    "Mike Hawthorn",
			    "Giuseppe Farina",
			    "Lando Norris",
			    "Arthur",
			    "Paulo",
			    "Leonardo",
			    "Marcelo",
			    "Mathias",
			    "Bino",
			    "Nícolas"
		));
		
		Country brasil = this.countryService.findCountryByBrazilian("Brasil");
		
		names.forEach((name) -> {
			Driver driver = new Driver();
			driver.setName(name);
			driver.setLevel(99);
			driver.setNationality(brasil);
			
			drivers.add(driver);
			
			this.driverRepo.save(driver);
		});
		
		return drivers;
	}

}
