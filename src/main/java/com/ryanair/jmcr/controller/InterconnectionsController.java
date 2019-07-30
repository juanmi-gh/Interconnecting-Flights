package com.ryanair.jmcr.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.ryanair.jmcr.model.Schedule;
import com.ryanair.jmcr.service.Converter;
import com.ryanair.jmcr.service.dto.Flight;
import com.ryanair.jmcr.service.routes.IRoutesConsumer;
import com.ryanair.jmcr.service.routes.IRoutesService;
import com.ryanair.jmcr.service.routes.dto.RouteAPI;
import com.ryanair.jmcr.service.routes.dto.RouteSearch;
import com.ryanair.jmcr.service.schedules.ISchedulesConsumer;
import com.ryanair.jmcr.service.schedules.ISchedulesService;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;
import com.ryanair.jmcr.service.schedules.dto.ScheduleSearch;
import com.ryanair.jmcr.utils.ValidationException; 

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
			log.warning(e.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private List<String> findInterconnectedLocations(FlightSearch flightSearch) {
	
		List<RouteAPI> apiRoutes = routesConsumer.collect();
		RouteSearch routeSearch = searchConverter.convert(flightSearch);
		return routesService.findStopLocations(apiRoutes, routeSearch);
	}

	private List<Flight> findFlighs(FlightSearch flightSearch, List<String> stopLocations) throws ValidationException {

		// Direct flights
		List<ScheduleSearch> schedulesSearch = buildSchedulesSearch(flightSearch);
		List<ScheduleAPI> routeSchedules = schedulesConsumer.findSchedules(schedulesSearch);
		List<Schedule> schedules = schedulesService.convert(schedulesSearch, routeSchedules);
		List<Schedule> validSchedules =  schedulesService.filterSchedules(flightSearch, schedules);
		List<Flight> flights =  schedulesService.buildFlights(flightSearch, validSchedules);
		
		// Interconnected flights
		for(String stopLocation : stopLocations) {
			FlightSearch leg1 = new FlightSearch(
									flightSearch.getDeparture(),
									stopLocation,
									flightSearch.getDepartureDateTime(),
									flightSearch.getArrivalDateTime());
			
			List<ScheduleSearch> leg1SchedulesSearch = buildSchedulesSearch(leg1);
			List<ScheduleAPI> leg1RouteSchedules = schedulesConsumer.findSchedules(leg1SchedulesSearch);
			List<Schedule> leg1Schedules = schedulesService.convert(leg1SchedulesSearch, leg1RouteSchedules);
			
		}
		
		return flights;
	}


	private List<ScheduleSearch> buildSchedulesSearch(FlightSearch flightSearch) {

		List<ScheduleSearch> result = new ArrayList<>();

    	List<LocalDate> dates = calculateYearsAndMonths(flightSearch.getDepartureDateTime(), flightSearch.getArrivalDateTime());
    	for(LocalDate date : dates) {

    		ScheduleSearch scheduleSearch = ScheduleSearch.builder()
    			.departure(flightSearch.getDeparture())
    			.arrival(flightSearch.getArrival())
    			.year(String.valueOf(date.getYear()))
    			.month(String.valueOf(date.getMonthValue()))
    			.build();

    		result.add(scheduleSearch);
    	}

		return result;
	}

	private List<LocalDate> calculateYearsAndMonths(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {

		List<LocalDate> result = new ArrayList<>();

		LocalDate departureDate = departureDateTime.toLocalDate().withDayOfMonth(1);
		LocalDate arrivalDate = arrivalDateTime.toLocalDate();

		while(departureDate.isBefore(arrivalDate) || departureDate.equals(arrivalDate)) {
			result.add(departureDate);
			departureDate = departureDate.plusMonths(1);
		}

		return result;
	}
	
}
