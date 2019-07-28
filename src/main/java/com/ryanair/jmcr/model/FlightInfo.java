package com.ryanair.jmcr.model;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightInfo {

	private final LocalTime departureTime;
	private final LocalTime arrivalTime;
	
}
