package com.ryanair.jmcr.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteLocations {

	private final String departureAirport;
	private final String arrivalAirport;
	
}
