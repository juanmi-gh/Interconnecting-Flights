package com.ryanair.jmcr.service.schedules.dto;

import lombok.Data;

@Data
public class ScheduleFlightAPI {

	private String number;
	private String departureTime;
	private String arrivalTime;
}
