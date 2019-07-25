package com.ryanair.jmcr.service.dto;

import lombok.Data;

@Data
public class ScheduleFlightAPI {

	private final String number;
	private final String departureTime;
	private final String arrivalTime;
}
