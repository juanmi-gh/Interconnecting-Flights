package com.ryanair.jmcr.service.schedules.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ScheduleAPI {

	private Integer month;
	private List<ScheduleDayAPI> days;
	
	public ScheduleAPI () {
		month = null;
		days = new ArrayList<>();
	}
}
