package com.ryanair.jmcr.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ryanair.jmcr.controller.dto.Flight;
import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.controller.dto.Leg;

@RestController
@RequestMapping(value = "seeker", produces = MediaType.APPLICATION_JSON_VALUE)
public class InterconnectionsController {


	@GetMapping("/interconnections")
	public ResponseEntity<Object> findFlights(
			@RequestParam(value="departure") String departure,
			@RequestParam(value="arrival") String arrival,
			@RequestParam(value="departureDateTime") String departureDateTime,
			@RequestParam(value="arrivalDateTime") String arrivalDateTime) {
		
		try {
			FlightSearch search = new FlightSearch(departure, arrival, departureDateTime, arrivalDateTime);
			List<Flight> result = mockFlights(search);
			
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private List<Flight> mockFlights(FlightSearch search) throws Exception {
		
		if (new Random().nextBoolean()) {
			throw new Exception("Time to fail!");
		}
		
		List<Flight> result = new ArrayList<>();
		
		Leg leg = new Leg(search.getDeparture(), search.getArrival(), "2018-03-01T12:40", "2018-03-01T16:40");
		List<Leg> legs = new ArrayList<>();
		legs.add(leg);
		Flight flight = new Flight(legs.size() - 1, legs);
		result.add(flight);
		
		legs = new ArrayList<>();
		leg = new Leg(search.getDeparture(), "STN", "2018-03-01T06:25", "2018-03-01T07:35");
		legs.add(leg);
		leg = new Leg("STN", search.getArrival(), "2018-03-01T09:50", "2018-03-01T13:20");
		legs.add(leg);
		
		flight = new Flight(legs.size() - 1, legs);
		result.add(flight);
		
		return result;
	}
}
