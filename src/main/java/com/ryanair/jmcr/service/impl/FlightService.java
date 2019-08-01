package com.ryanair.jmcr.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.model.FlightInfo;
import com.ryanair.jmcr.model.Schedule;
import com.ryanair.jmcr.service.IFlightService;
import com.ryanair.jmcr.service.dto.Flight;
import com.ryanair.jmcr.service.dto.Leg;

@Service
public class FlightService implements IFlightService {

	@Override
	public List<Leg> buildLegs(FlightSearch flightSearch, List<Schedule> schedules) {

		List<Leg> result = new ArrayList<>();
		
		for(Schedule day : schedules) {
			for(FlightInfo info : day.getFlights()) {

				Leg leg = buildLeg(flightSearch, day.getDate(), info);
				result.add(leg);
			}
		}
		
		return result;
	}
	
	@Override
	public List<Flight> buildFlights(FlightSearch flightSearch, List<Schedule> schedules) {
		
		List<Flight> result = new ArrayList<>();
		
		for(Schedule day : schedules) {
			for(FlightInfo info : day.getFlights()) {
				
				Flight flight = buildFlight(flightSearch, day.getDate(), info);
				result.add(flight);				
			}
		}
		
		return result;
	}
	
	@Override
	public List<Flight> buildFlights(List<Leg> firstPartLegs, List<Leg> secondPartLegs) {

		List<Flight> result = new ArrayList<>();
		
		for(Leg firstPartLeg  : firstPartLegs) {
			for(Leg secondPartLeg  : secondPartLegs) {
				if (firstPartLeg.isValidConnection(secondPartLeg)) {
					Flight flight = buildFlight(firstPartLeg, secondPartLeg);
					result.add(flight);
				}
			}
		}
		
		
		return result;
	}
	
	private Leg buildLeg(FlightSearch flightSearch, LocalDate date, FlightInfo info) {
		
		String departureDateTime = LocalDateTime.of(date, info.getDepartureTime()).toString();
		String arrivalDateTime = LocalDateTime.of(date, info.getArrivalTime()).toString();
		
		return  Leg.builder()
    				.departureAirport(flightSearch.getDeparture())
    				.arrivalAirport(flightSearch.getArrival())
    				.departureDateTime(departureDateTime) 
    				.arrivalDateTime(arrivalDateTime)
    				.build();
	}
	
	private Flight buildFlight(Leg leg1, Leg leg2) {
		
		return Flight.builder()
						.stops(1)
						.legs(List.of(leg1, leg2))
						.build();
	}
	
	private Flight buildFlight(FlightSearch flightSearch, LocalDate date, FlightInfo info) {
		
		Leg leg = buildLeg(flightSearch, date, info);
		
		return Flight.builder()
					.stops(0)
					.legs(List.of(leg))
					.build();
	}

}
