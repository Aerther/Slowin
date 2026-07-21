package com.f1project.request;

import java.time.LocalDateTime;
import java.util.List;

import com.f1project.model.entity.Weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaceRequest {
	private Long id;
	
	private String name;
	
	private int lapsQuantity;
	
	private Long trackId;
	
    private List<Long> driversId;
}
