package com.ryanair.jmcr.service.dto;

import java.util.List;

import lombok.Data;

@Data
public class ScheduleDayAPI {

	private final Integer day;
	private final List<ScheduleFlightAPI> flights;
}
