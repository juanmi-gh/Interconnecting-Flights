package com.ryanair.jmcr.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ryanair.jmcr.controller.dto.FailResponse;
import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.model.Schedule;
import com.ryanair.jmcr.service.Converter;
import com.ryanair.jmcr.service.IFlightService;
import com.ryanair.jmcr.service.dto.Flight;
import com.ryanair.jmcr.service.dto.Leg;
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
public class InterconnectionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger("CONTROLLER");
	
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

	@Autowired
	IFlightService flightsService;
	
	@GetMapping(value = "/interconnections")
	public ResponseEntity<Object> findFlights(
			@RequestParam(value="departure") String departure,
			@RequestParam(value="arrival") String arrival,
			@RequestParam(value="departureDateTime") String departureDateTime,
			@RequestParam(value="arrivalDateTime") String arrivalDateTime) {

		try {
			LOGGER.info("Petición recibida");
			FlightSearch flightSearch = new FlightSearch(departure, arrival, departureDateTime, arrivalDateTime);
			LOGGER.info("FlightSearch: ");
			LOGGER.info(flightSearch.toString());
			
			List<String> stopLocations = findInterconnectedLocations(flightSearch);
			LOGGER.info("Conexiones encontradas: {}", stopLocations.size());
			
			List<Flight> resultFlights = findFlighs(flightSearch, stopLocations);
			LOGGER.info("Vuelos encontrados: {}", resultFlights.size());

			return new ResponseEntity<>(resultFlights, HttpStatus.OK);

		} catch (Exception e) {
			LOGGER.warn(e.getMessage(), e);
			FailResponse response = new FailResponse(e.getClass().getName(), e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private List<String> findInterconnectedLocations(FlightSearch flightSearch) {
	
		List<RouteAPI> apiRoutes = routesConsumer.collect();
		LOGGER.info("Rutas encontradas: {}", apiRoutes.size());
		
		RouteSearch routeSearch = searchConverter.convert(flightSearch);
		LOGGER.info("Route search: {}", routeSearch.toString());
		
		return routesService.findStopLocations(apiRoutes, routeSearch);
	}

	private List<Flight> findFlighs(FlightSearch flightSearch, List<String> stopLocations) throws ValidationException {

		List<Flight> flights = new ArrayList<>();
		List<Flight> interconnectedFlights = new ArrayList<>();

		// Direct flights
		List<ScheduleSearch> schedulesSearch = buildSchedulesSearch(flightSearch);
		List<ScheduleAPI> routeSchedules = schedulesConsumer.findSchedules(schedulesSearch);
		LOGGER.info("Vuelo directo | Schedules encontrados: {}", routeSchedules.size());

		List<Schedule> schedules = schedulesService.convert(schedulesSearch, routeSchedules);
		List<Schedule> validSchedules =  schedulesService.filterSchedules(flightSearch, schedules);
		LOGGER.info("Vuelo directo | Schedules válidos: {}", validSchedules.size());

		List<Flight> directFlights =  flightsService.buildFlights(flightSearch, validSchedules);
		LOGGER.info("Vuelos directos: {}", directFlights.size());

		// Interconnected flights
		for(String stopLocation : stopLocations) {
			LOGGER.info("Stop localition: {}", stopLocation);
			FlightSearch leg1 = new FlightSearch(
									flightSearch.getDeparture(),
									stopLocation,
									flightSearch.getDepartureDateTime(),
									flightSearch.getArrivalDateTime());

			List<ScheduleSearch> leg1SchedulesSearch = buildSchedulesSearch(leg1);
			List<ScheduleAPI> leg1RouteSchedules = schedulesConsumer.findSchedules(leg1SchedulesSearch);
			List<Schedule> leg1Schedules = schedulesService.convert(leg1SchedulesSearch, leg1RouteSchedules);
			List<Schedule> leg1ValidSchedules = schedulesService.filterSchedules(leg1, leg1Schedules);
			List<Leg> firstPartLegs = flightsService.buildLegs(leg1, leg1ValidSchedules);

			FlightSearch leg2 = new FlightSearch(
					stopLocation,
					flightSearch.getArrival(),
					flightSearch.getDepartureDateTime(),
					flightSearch.getArrivalDateTime());

            List<ScheduleSearch> leg2SchedulesSearch = buildSchedulesSearch(leg2);
            List<ScheduleAPI> leg2RouteSchedules = schedulesConsumer.findSchedules(leg2SchedulesSearch);
            List<Schedule> leg2Schedules = schedulesService.convert(leg2SchedulesSearch, leg2RouteSchedules);
            List<Schedule> leg2ValidSchedules = schedulesService.filterSchedules(leg2, leg2Schedules);
            List<Leg> secondPartLegs = flightsService.buildLegs(leg2, leg2ValidSchedules);

            List<Flight> stopLocationFlights = flightsService.buildFlights(firstPartLegs, secondPartLegs);

            interconnectedFlights.addAll(stopLocationFlights);
		}

		flights.addAll(directFlights);
		flights.addAll(interconnectedFlights);

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
