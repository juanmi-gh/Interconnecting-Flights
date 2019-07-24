package com.ryanair.jmcr.controller.dto;

import lombok.Data;

@Data
public class FlightSearch {

	private final String departure;
	private final String arrival;
	private final String departureDateTime;
	private final String arrivalDateTime;
}
