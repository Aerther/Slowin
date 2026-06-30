package com.f1project.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRequest {
	private Long id;
	private String name;
	private int level;
	private Long countryId;
}
