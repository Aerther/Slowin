package com.f1project.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.f1project.model.dto.CountryDTO;
import com.f1project.model.dto.TrackDTO;

public class ReadFile {
	public static ArrayList<TrackDTO> readTracksFile() {
		File file = new File("data/tracks.txt");
		
		ArrayList<TrackDTO> tracks = new ArrayList<>();
		
		try {
			FileReader reader = new FileReader(file);
			BufferedReader buffer = new BufferedReader(reader);
			
			String line = buffer.readLine();
			while((line = buffer.readLine()) != null) {
				String[] attrs = line.split(";");
				
				String name = attrs[0];
				String city = attrs[2];
				String country = attrs[6];
				
				double fastestTime = Double.parseDouble(attrs[1]);
				double latitude = Double.parseDouble(attrs[3]);
				double longitude = Double.parseDouble(attrs[4]);
				double length = Double.parseDouble(attrs[5]);
				length = Math.round(length * 1000.0) / 1000.0;
				
				CountryDTO countryDTO = new CountryDTO();
				countryDTO.setBrazilian(country);
				
				TrackDTO trackDTO = new TrackDTO(
					null,
					name,
					city,
					fastestTime,
					FormatUtils.formatLapTime(fastestTime),
					length,
					latitude,
					longitude,
					countryDTO
				);
				
				tracks.add(trackDTO);
			}
			
			buffer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tracks;
	}
}
