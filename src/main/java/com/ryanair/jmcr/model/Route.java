package com.ryanair.jmcr.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Route {

	private String departureAirport;
	private String arrivalAirport;
	private String departureDateTime;
	private String arrivalDateTime;
	
}
