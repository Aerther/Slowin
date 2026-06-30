package com.f1project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
	private Long id;
	private String name;
	private int level;
	private CountryDTO nationality;
}
