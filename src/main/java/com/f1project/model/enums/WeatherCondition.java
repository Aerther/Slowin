package com.f1project.model.enums;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeatherCondition {
    SUNNY("Ensolarado", "sunny.png", true, List.of(0) ),
    OVERCAST("Encoberto", "overcast.png", true, List.of(1, 2, 3) ),
    CLOUDY("Nublado", "cloudy.png", true, List.of(45, 48) ),
    DIZZLE("Chuva Leve", "dizzle.png", false, List.of(51, 53, 55, 56, 57) ),
    RAINY("Chuvoso", "rainy.png", false, List.of(61, 63, 65, 66, 67, 80, 81, 82) ),
    STORMY("Tempestade", "stormy.png", false, List.of(95, 96, 99) ),
    SNOWY("Nevando", "snowy.png", false, List.of(71, 73, 75, 77, 85, 86) );

    private final String description;
    private final String imageName;
    private final boolean isDry;
    private final List<Integer> codes;
    
    public static WeatherCondition fromCode(int code) {
        return Arrays.stream(WeatherCondition.values())
        		.filter(condition -> condition.getCodes().contains(code))
                .findFirst()
                .orElse(SUNNY);
    }
    
    public String getWeatherUrl() {
    	return "/images/weather/" + imageName;
    }
    
    public List<WeatherCondition> getNextWeatherPossibleConditions() {
    	return switch (this) {
	        case SUNNY -> List.of(OVERCAST, CLOUDY);
	        case OVERCAST -> List.of(SUNNY, CLOUDY);
	        case CLOUDY -> List.of(OVERCAST, DIZZLE);
	        case DIZZLE -> List.of(CLOUDY, RAINY);
	        case RAINY -> List.of(DIZZLE, STORMY);
	        case STORMY -> List.of(RAINY, SNOWY);
	        case SNOWY -> List.of(STORMY, RAINY);
	    };
    }
    
    public int getProbabilityToChangeToFirst() {
    	return switch (this) {
	        case SUNNY -> 70;
	        case OVERCAST -> 50;
	        case CLOUDY -> 60;
	        case DIZZLE -> 40;
	        case RAINY -> 70;
	        case STORMY -> 80;
	        case SNOWY -> 90;
	    };
    }
}