package com.ryanair.jmcr.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Schedule {

	private LocalDate date;
	private List<FlightInfo> flights;
	
}
