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
import com.ryanair.jmcr.service.Consumer;
import com.ryanair.jmcr.service.routes.dto.RouteAPI; 


@RestController
@RequestMapping("seeker")
@lombok.extern.java.Log
public class InterconnectionsController {

	@Autowired
	Consumer<RouteAPI> routesConsumer;
	
//	@Autowired
//	RoutesService routesService;
//	
//	@Autowired
//	SchedulesConsumer schedulesConsumer;
//	
//	@Autowired
//	SchedulesService schedulesService;

	@GetMapping(value = "/interconnections")
	public ResponseEntity<Object> findFlights(
			@RequestParam(value="departure") String departure,
			@RequestParam(value="arrival") String arrival,
			@RequestParam(value="departureDateTime") String departureDateTime,
			@RequestParam(value="arrivalDateTime") String arrivalDateTime) {
		
		try {
			FlightSearch search = new FlightSearch(departure, arrival, departureDateTime, arrivalDateTime);
			
			/**
			 * 1. Collect Routes
			 * 2. Map-Reduce Routes
			 * 3. Collect Schedules
			 * 4. Map-Reduce Schedules
			 */
			List<RouteAPI> apiRoutes = routesConsumer.collect();
//			List<Route> routes = routesService.filter(apiRoutes, search);
//			List<ScheduleAPI> apiSchedules = schedulesConsumer.collectByRoutes(routes);
			// TODO
			
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
			
		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	


}
