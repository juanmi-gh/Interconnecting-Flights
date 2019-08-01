package com.ryanair.jmcr.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Leg implements Serializable {

	private static final long serialVersionUID = -1378577773269353637L;

	private final String departureAirport;
	private final String arrivalAirport;
	private final String departureDateTime;
	private final String arrivalDateTime;
	
	public boolean isValidConnection(Leg leg) {
		
		LocalDateTime firstLegArrival = LocalDateTime.parse(arrivalDateTime);
		LocalDateTime secondLegDeparture = LocalDateTime.parse(leg.departureDateTime);
		
		long hoursBetweenConnection = ChronoUnit.HOURS.between(firstLegArrival, secondLegDeparture);
		
		return 2 <= hoursBetweenConnection;
	}
}
