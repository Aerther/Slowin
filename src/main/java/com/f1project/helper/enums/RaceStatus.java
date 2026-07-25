package com.f1project.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RaceStatus {
	FINISHED("Terminada"),
	ONGOING("Em andamento"),
	CREATED("Criada");
	
	private final String description;
}
