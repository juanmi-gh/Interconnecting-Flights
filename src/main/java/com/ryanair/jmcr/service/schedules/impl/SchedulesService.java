package com.ryanair.jmcr.service.schedules.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.model.FlightInfo;
import com.ryanair.jmcr.model.Schedule;
import com.ryanair.jmcr.service.dto.Flight;
import com.ryanair.jmcr.service.dto.Leg;
import com.ryanair.jmcr.service.schedules.ISchedulesService;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;

@Service
public class SchedulesService implements ISchedulesService {

	@Override
	public List<Flight> filterSchedules(FlightSearch flightSearch, List<Schedule> dailySchedules) {

		List<Flight> result = new ArrayList<>();

		List<Schedule> filteredSchedules = filterSchedulesByDate(flightSearch, dailySchedules);
				
		LocalTime departureTime = flightSearch.getDepartureDateTime().toLocalTime();
		LocalTime arrivalTime = flightSearch.getArrivalDateTime().toLocalTime();
		
		for(Schedule day : filteredSchedules) {
			for(FlightInfo info : day.getFlights()) {

				if (info.getDepartureTime().isBefore(departureTime) ||
					info.getArrivalTime().isAfter(arrivalTime)) {
					continue;
				}

				Flight flight = buildFlight(flightSearch, day.getDate(), info);
				result.add(flight);
			}
		}

		return result;
	}

	private List<Schedule> filterSchedulesByDate(FlightSearch flightSearch, List<Schedule> dailySchedules) {
		
		LocalDate departureDate = flightSearch.getDepartureDateTime().toLocalDate();
		LocalDate arrivalDate = flightSearch.getArrivalDateTime().toLocalDate();

		return dailySchedules.stream()
				. filter(day -> (departureDate.isBefore(day.getDate()) || departureDate.equals(day.getDate()))
							&& (arrivalDate.isAfter(day.getDate()) || arrivalDate.equals(day.getDate())))
				.collect(Collectors.toList());
	}
	
	private Flight buildFlight(FlightSearch flightSearch, LocalDate date, FlightInfo info) {
		
		String departure = LocalDateTime.of(date, info.getDepartureTime()).toString();
		String arrival = LocalDateTime.of(date, info.getArrivalTime()).toString();
		
		Leg leg = Leg.builder()
    				.departureAirport(flightSearch.getDeparture())
    				.arrivalAirport(flightSearch.getArrival())
    				.departureDateTime(departure) 
    				.arrivalDateTime(arrival)
    				.build();
		
		return Flight.builder()
					.stops(0)
					.legs(List.of(leg))
					.build();
	}

	@Override
	public List<Flight> matchConnections(List<ScheduleAPI> firstLegSchedules, List<ScheduleAPI> secondLegSchedules) {
		return new ArrayList<>();
	}

}
