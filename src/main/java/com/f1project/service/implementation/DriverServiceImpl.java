package com.f1project.service.implementation;

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

}
