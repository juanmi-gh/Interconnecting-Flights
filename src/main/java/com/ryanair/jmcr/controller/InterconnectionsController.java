package com.ryanair.jmcr.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ryanair.jmcr.controller.dto.Flight;
import com.ryanair.jmcr.controller.dto.Leg;

@RestController
@RequestMapping(value = "seeker", produces = MediaType.APPLICATION_JSON_VALUE)
public class InterconnectionsController {


	@GetMapping("/interconnections")
	public List<Flight> findFlights() {
		
		return mockFlights();
	}
	
	private List<Flight> mockFlights() {
		
		List<Flight> result = new ArrayList<>();
		
		Leg leg = new Leg("DUB", "WRO", "2018-03-01T12:40", "2018-03-01T16:40");
		List<Leg> legs = new ArrayList<>();
		legs.add(leg);
		Flight flight = new Flight(legs.size() - 1, legs);
		result.add(flight);
		
		legs = new ArrayList<>();
		leg = new Leg("DUB", "STN", "2018-03-01T06:25", "2018-03-01T07:35");
		legs.add(leg);
		leg = new Leg("STN", "WRO", "2018-03-01T09:50", "2018-03-01T13:20");
		legs.add(leg);
		
		flight = new Flight(legs.size() - 1, legs);
		result.add(flight);
		
		return result;
	}
}
