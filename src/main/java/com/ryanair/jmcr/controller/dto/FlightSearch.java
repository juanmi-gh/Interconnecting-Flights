package com.ryanair.jmcr.controller.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ryanair.jmcr.utils.ValidationException;

import lombok.Data;

@Data
public class FlightSearch {

	private final String departure;
	private final String arrival;
	private final LocalDateTime departureDateTime;
	private final LocalDateTime arrivalDateTime;
	
	public FlightSearch(String departure, String arrival, String departureDateTime, String arrivalDateTime) throws ValidationException {
		
		this.departure = departure;
		this.arrival = arrival;
		this.departureDateTime = LocalDateTime.parse(departureDateTime, DateTimeFormatter.ISO_DATE_TIME);
		this.arrivalDateTime = LocalDateTime.parse(arrivalDateTime, DateTimeFormatter.ISO_DATE_TIME);

		if (this.departureDateTime.isAfter(this.arrivalDateTime)) {
			throw new ValidationException("Incorrect dates.");
		}
	}
}
