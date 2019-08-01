package com.ryanair.jmcr.service.routes.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.jmcr.model.RouteLocations;
import com.ryanair.jmcr.model.Stop;
import com.ryanair.jmcr.service.Converter;
import com.ryanair.jmcr.service.routes.IRoutesService;
import com.ryanair.jmcr.service.routes.dto.RouteAPI;
import com.ryanair.jmcr.service.routes.dto.RouteSearch;

@Service
public class RoutesService implements IRoutesService {

	private static final String OPERATOR = "RYANAIR";

	@Autowired
	Converter<RouteLocations, RouteAPI> converter;

	@Override
	public List<String> findStopLocations(List<RouteAPI> apiRoutes, RouteSearch search) {

		List<RouteLocations> validRoutes = apiRoutes.parallelStream()
                                        			.filter(RoutesService::isValid)
                                        			.map(converter::convert)
                                        			.collect(Collectors.toList());

		List<Stop> stops = findStops(search, validRoutes);

		return filterInterconnectedStops(stops);
	}

	protected static Boolean isValid(RouteAPI route) {
		
		return OPERATOR.equals(route.getOperator())
				&& route.getConnectingAirport() == null;
	}

	private List<Stop> findStops(RouteSearch search, List<RouteLocations> routes) {

		Map<String, Stop> potentialStops = new HashMap<>();
		
		for (RouteLocations route : routes) {
		
			Stop potentialStop = checkPotencialStop(search, route);
			registerStop(potentialStop, potentialStops);
		}
		
		return new ArrayList<>(potentialStops.values());
	}

	private void registerStop(Stop potentialStop, Map<String, Stop> potentialStops) {

		if (potentialStop.isEmpty()) {
			return;
		}
		
		Stop previousDetectedStop = potentialStops.get(potentialStop.getAirport());

		if (previousDetectedStop == null) {
			potentialStops.put(potentialStop.getAirport(), potentialStop);
		
		} else {
			previousDetectedStop.join(potentialStop);
		}
	}
	
	private Stop checkPotencialStop(RouteSearch search, RouteLocations route) {
		
		if (isArrivalStop(search, route)) {
			return Stop.builder()
					.airport(route.getArrivalAirport())
					.isArrival(Boolean.TRUE)
					.build();
		}
		
		if (isDepartureStop(search, route)) {
			return Stop.builder()
					.airport(route.getDepartureAirport())
					.isDeparture(Boolean.TRUE)
					.build();
		}
		
		return Stop.builder().build();
	}
	
	private Boolean isArrivalStop(RouteSearch search, RouteLocations route) {
		return search.getDeparture().equals(route.getDepartureAirport()) 
			&& !search.getArrival().equals(route.getArrivalAirport());
	}
	
	private Boolean isDepartureStop(RouteSearch search, RouteLocations route) {
		return !search.getDeparture().equals(route.getDepartureAirport()) 
			&& search.getArrival().equals(route.getArrivalAirport());
	}

	private List<String> filterInterconnectedStops(List<Stop> stops) {
		
		return stops.parallelStream()
				.filter(Stop::isInterconnected)
				.map(Stop::getAirport)
				.collect(Collectors.toList());
	}

}
