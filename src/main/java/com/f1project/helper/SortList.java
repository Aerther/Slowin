package com.f1project.helper;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.f1project.model.dto.CountryDTO;
import com.f1project.model.dto.DriverDTO;
import com.f1project.model.dto.RaceDTO;
import com.f1project.model.dto.TrackDTO;

public class SortList {
	
	private static final Collator PT_BR = Collator.getInstance(new Locale("pt", "BR"));

	static {
	    PT_BR.setStrength(Collator.PRIMARY);
	}

	private static <T> List<T> sort(
	        List<T> list,
	        Function<T, String> keyExtractor) {

	    return list.stream()
	            .sorted(Comparator.comparing(keyExtractor, PT_BR))
	            .collect(Collectors.toList());
	}
	
	public static List<CountryDTO> sortCountriesByName(List<CountryDTO> countries) {
		return sort(countries, CountryDTO::getBrazilian);
	}
	
	public static List<TrackDTO> sortTracksByName(List<TrackDTO> tracks) {
		return sort(tracks, TrackDTO::getName);
	}
	
	public static List<DriverDTO> sortDriversByName(List<DriverDTO> drivers) {
		return sort(drivers, DriverDTO::getName);
	}
	
	public static List<RaceDTO> sortRacesByName(List<RaceDTO> races) {
		return sort(races, RaceDTO::getName);
	}
}
