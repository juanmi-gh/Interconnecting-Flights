package com.ryanair.jmcr.service.schedules.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleSearch {

	private final String departure;
	private final String arrival;
	private final String year;
	private final String month;
}
