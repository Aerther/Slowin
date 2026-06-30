package com.f1project.service;

import java.util.List;

import com.f1project.model.dto.DriverDTO;
import com.f1project.model.entity.Driver;
import com.f1project.request.DriverRequest;

public interface DriverService {
	List<Driver> findAllDrivers();
	
	Driver findDriverById(Long id);
	
	Driver saveDriver(DriverRequest driverRequest);
	
	Driver updateDriver(DriverRequest driverRequest);
	
	void deleteDriver(Long id);
	
	void deleteAllDrivers();
}
