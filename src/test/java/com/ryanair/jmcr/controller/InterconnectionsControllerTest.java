package com.ryanair.jmcr.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ryanair.jmcr.controller.dto.Flight;
import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.controller.dto.Leg;
import com.ryanair.jmcr.service.Consumer;
import com.ryanair.jmcr.service.routes.dto.RouteAPI;

import lombok.extern.java.Log;

@Log
public class InterconnectionsControllerTest {

	private final String DEPARTURE = "DUB";
	private final String ARRIVAL = "WRO";
	private final String DEPARTURE_DATE_TIME = "2018-03-01T07:00";
	private final String ARRIVAL_DATE_TIME = "2018-03-03T21:00";
	
	@InjectMocks
	private InterconnectionsController controller;
	
	@Mock
	Consumer<RouteAPI> routesConsumer;
	
	private MockMvc mockMvc;
	private RequestBuilder request;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		
		request = get("/seeker/interconnections")
				.param("departure", DEPARTURE)
				.param("arrival", ARRIVAL)
				.param("departureDateTime", DEPARTURE_DATE_TIME)
				.param("arrivalDateTime", ARRIVAL_DATE_TIME);
		
		log.info("Request created: " + request.toString());
	}

	// FIXME
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
	
	@Test
	public void testValidResponse() throws Exception {
		
		// TODO
		
		mockMvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			;
	}
	
	@Test
	public void testServerError() throws Exception {

		when(routesConsumer.collect()).thenThrow(new RuntimeException());
		
		mockMvc.perform(request)
			.andExpect(status().isInternalServerError())
			;
	}
}
