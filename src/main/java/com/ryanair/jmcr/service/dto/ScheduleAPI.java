package com.ryanair.jmcr.service.dto;

import java.util.List;

import lombok.Data;

@Data
public class ScheduleAPI {

	private final Integer month;
	private final List<ScheduleDayAPI> days;
}
