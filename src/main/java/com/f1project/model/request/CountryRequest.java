package com.f1project.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryRequest {
	private Long id;
	
	private String name;
	
	private String capital;
	
	private String code;
	
	private String flagUrl;
	
	private String brazilian;
}
