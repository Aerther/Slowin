package com.f1project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
	private Long id;
	private String name;
	private String capital;
	private String code;
	private String flagUrl;
	private String brazilian;
}
