package com.ryanair.jmcr.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.service.Converter;
import com.ryanair.jmcr.service.dto.Flight;
import com.ryanair.jmcr.service.routes.IRoutesConsumer;
import com.ryanair.jmcr.service.routes.IRoutesService;
import com.ryanair.jmcr.service.routes.dto.RouteAPI;
import com.ryanair.jmcr.service.routes.dto.RouteSearch;
import com.ryanair.jmcr.service.schedules.ISchedulesConsumer;
import com.ryanair.jmcr.service.schedules.ISchedulesService;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI; 

@RestController
@RequestMapping("seeker")
@lombok.extern.java.Log
public class InterconnectionsController {

	@Autowired
	Converter<RouteSearch, FlightSearch> searchConverter;

	@Autowired
	IRoutesConsumer routesConsumer;

	@Autowired
	IRoutesService routesService;

	@Autowired
	ISchedulesConsumer schedulesConsumer;

	@Autowired
	ISchedulesService schedulesService;

	@GetMapping(value = "/interconnections")
	public ResponseEntity<Object> findFlights(
			@RequestParam(value="departure") String departure,
			@RequestParam(value="arrival") String arrival,
			@RequestParam(value="departureDateTime") String departureDateTime,
			@RequestParam(value="arrivalDateTime") String arrivalDateTime) {

		try {
			FlightSearch flightSearch = new FlightSearch(departure, arrival, departureDateTime, arrivalDateTime);
			List<String> stopLocations = findInterconnectedLocations(flightSearch);			
			List<Flight> resultFlights = findFlighs(flightSearch, stopLocations);

			return new ResponseEntity<>(resultFlights, HttpStatus.OK);

		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private List<String> findInterconnectedLocations(FlightSearch flightSearch) {
	
		RouteSearch routeSearch = searchConverter.convert(flightSearch);
		List<RouteAPI> apiRoutes = routesConsumer.collect();
		return routesService.findStopLocations(apiRoutes, routeSearch);
	}

	private List<Flight> findFlighs(FlightSearch flightSearch, List<String> stopLocations) {
		
		List<Flight> resultFlights = new ArrayList<>();
		
		for (String stop : stopLocations) {
			List<ScheduleAPI> firstLegSchedules = schedulesConsumer.findFirstLeg(flightSearch, stop);
			List<ScheduleAPI> secondLegSchedules = schedulesConsumer.findSecondLeg(flightSearch, stop);
			
			List<Flight> flights = schedulesService.matchConnections(firstLegSchedules, secondLegSchedules);
			resultFlights.addAll(flights);
		}
		
		return resultFlights;
	}
}
