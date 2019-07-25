package com.ryanair.jmcr.service.dto;

import java.util.List;

import lombok.Data;

@Data
public class ScheduleAPI {

	private Integer month;
	private List<ScheduleDayAPI> days;
}
