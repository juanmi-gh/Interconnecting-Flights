package com.ryanair.jmcr.model;

import lombok.Data;

@Data
public class Route {

	private final String departureAirport;
	private final String arrivalAirport;
	private final String departureDateTime;
	private final String arrivalDateTime;
	
}
