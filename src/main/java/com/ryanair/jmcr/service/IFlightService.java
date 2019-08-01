package com.ryanair.jmcr.service;

import java.util.List;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.model.Schedule;
import com.ryanair.jmcr.service.dto.Flight;
import com.ryanair.jmcr.service.dto.Leg;

public interface IFlightService {

	List<Leg> buildLegs(FlightSearch flightSearch, List<Schedule> schedules);
	
	List<Flight> buildFlights(FlightSearch flightSearch, List<Schedule> schedules);
	
	List<Flight> buildFlights(List<Leg> firstPartLegs, List<Leg> secondPartLegs);
}
