package com.f1project.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.f1project.client.ApiClient;
import com.f1project.exception.ResourceNotFoundException;
import com.f1project.mapper.CentralMapper;
import com.f1project.model.dto.CountryDTO;
import com.f1project.model.entity.Country;
import com.f1project.model.request.CountryRequest;
import com.f1project.repository.CountryRepository;
import com.f1project.service.CountryService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CountryServiceImpl implements CountryService {
	
	private CountryRepository countryRepo;
	private CentralMapper mapper;
	private ApiClient client;

	@Override
	public List<Country> findAllCountries() {
		return this.countryRepo.findAll();
	}

	@Override
	public Country findCountryById(Long id) {
		return this.countryRepo.findById(id).orElseThrow(() -> {throw new ResourceNotFoundException("A COUNTRY with ID=" + id + " doesn't exists");});
	}

	@Override
	public Country findCountryByName(String name) {
		return this.countryRepo.findByName(name).orElseThrow(() -> {throw new ResourceNotFoundException("A COUNTRY with NAME=" + name + " doesn't exists");});
	}
	
	@Override
	public Country findCountryByBrazilian(String name) {
		return this.countryRepo.findByBrazilian(name).orElseThrow(() -> {throw new ResourceNotFoundException("A COUNTRY with BRAZILIAN=" + name + " doesn't exists");});
	}

	@Override
	public Country saveCountry(CountryRequest countryRequest) {
		Country country = mapper.request2Country(countryRequest);
		
		Country savedCountry = this.countryRepo.save(country);
		
		return savedCountry;
	}

	@Override
	public void deleteCountry(Long id) {
		this.findCountryById(id);
		
		this.countryRepo.deleteById(id);
	}

	@Override
	public void deleteAllCountries() {
		this.countryRepo.deleteAll();
	}

	@Override
	public Country updateCountry(CountryRequest countryRequest) {
		this.findCountryById(countryRequest.getId());
		
		Country country = mapper.request2Country(countryRequest);
		country.setId(countryRequest.getId());
		
		Country savedCountry = this.countryRepo.save(country);
		
		return savedCountry;
	}

	@Override
	public List<Country> saveCountriesFromClient() {
		List<CountryDTO> countriesDTO = client.getRestCountries();
		
		List<Country> countries = countriesDTO.stream().map(mapper::DTO2country).collect(Collectors.toList());
		
		List<Country> savedCountries = this.countryRepo.saveAll(countries);
		
		return savedCountries;
	}

	@Override
	public List<Country> findAllCountriesOrderByBrazilianAsc() {
		return this.countryRepo.findAllByOrderByBrazilianAsc();
	}
}
