package com.f1project.client.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponse {
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("capital")
	private String capital;
	
	@JsonProperty("alpha3Code")
	private String code;
	
	@JsonProperty("flags")
	private Map<String, String> flags;
	
	@JsonProperty("translations")
	private Map<String, String> translations;
	
	@JsonProperty("independent")
	private boolean independent;
}
