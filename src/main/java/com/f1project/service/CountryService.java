package com.f1project.service;

import java.util.List;

import com.f1project.model.dto.CountryDTO;
import com.f1project.model.entity.Country;
import com.f1project.request.CountryRequest;

public interface CountryService {
	public List<Country> findAllCountries();
	
	public Country findCountryById(Long id);
	
	public Country findCountryByName(String name);
	public Country findCountryByBrazilian(String brazilian);
	
	public Country saveCountry(CountryRequest countryRequest);
	
	public List<Country> saveCountriesFromClient();
	
	public Country updateCountry(CountryRequest countryRequest);
	
	void deleteCountry(Long id);
	
	void deleteAllCountries();
}
