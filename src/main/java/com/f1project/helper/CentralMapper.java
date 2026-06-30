package com.f1project.helper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.f1project.model.dto.CountryDTO;
import com.f1project.model.dto.DriverDTO;
import com.f1project.model.dto.RaceDTO;
import com.f1project.model.dto.RaceResultDTO;
import com.f1project.model.dto.TrackDTO;
import com.f1project.model.dto.WeatherDTO;
import com.f1project.model.entity.Country;
import com.f1project.model.entity.Driver;
import com.f1project.model.entity.Race;
import com.f1project.model.entity.RaceResult;
import com.f1project.model.entity.Track;
import com.f1project.model.entity.Weather;
import com.f1project.request.CountryRequest;
import com.f1project.request.DriverRequest;
import com.f1project.request.RaceRequest;
import com.f1project.request.TrackRequest;

@Mapper(componentModel = "spring")
public interface CentralMapper {
	DriverDTO driver2DTO(Driver driver);
	Driver DTO2driver(DriverDTO driverDTO);
	
	Driver request2Driver(DriverRequest driverRequest);
	
	CountryDTO country2DTO(Country country);
	Country DTO2country(CountryDTO countryDTO);
	
	Country request2Country(CountryRequest countryRequest);
	
	@Mapping(source = "country", target = "countryDTO")
	TrackDTO track2DTO(Track track);
	
	@Mapping(source = "countryDTO", target = "country")
	Track DTO2track(TrackDTO trackDTO);
	
	Track request2Track(TrackRequest trackRequest);
	
	@Mapping(source = "track", target = "trackDTO")
    @Mapping(source = "weather", target = "weatherDTO")
    @Mapping(source = "results", target = "resultsDTO")
    RaceDTO race2DTO(Race race);

    @Mapping(source = "trackDTO", target = "track")
    @Mapping(source = "weatherDTO", target = "weather")
    @Mapping(source = "resultsDTO", target = "results")
    Race DTO2race(RaceDTO raceDTO);
	
	Race request2Race(RaceRequest raceRequest);
	
	RaceResultDTO raceResult2DTO(RaceResult raceResult);
	RaceResult DTO2raceResult(RaceResultDTO raceResultDTO);
	
	Weather DTO2weather(WeatherDTO weatherDTO);
	
	List<CountryDTO> countries2DTOList(List<Country> countries);
	List<RaceDTO> races2DTOList(List<Race> races);
	List<DriverDTO> drivers2DTOList(List<Driver> drivers);
	List<TrackDTO> tracks2DTOList(List<Track> tracks);
}
