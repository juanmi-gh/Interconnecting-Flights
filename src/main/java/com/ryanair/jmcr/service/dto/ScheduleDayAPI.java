package com.ryanair.jmcr.service.dto;

import java.util.List;

import lombok.Data;

@Data
public class ScheduleDayAPI {

	private Integer day;
	private List<ScheduleFlightAPI> flights;
}
