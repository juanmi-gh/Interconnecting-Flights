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
	
	private static Boolean isValid(RouteAPI route) {
		
		return OPERATOR.equals(route.getOperator())
				&& route.getConnectingAirport() == null;
	}

	private List<Stop> findStops(RouteSearch search, List<RouteLocations> routes) {

		Map<String, Stop> potentialStops = new HashMap<>();
		
		for (RouteLocations route : routes) {
		
			Stop potentialStop = checkPotencialStop(search, route);
			if (potentialStop.isEmpty()) {
				continue;
			}
			
			Stop previousDetectedStop = potentialStops.get(potentialStop.getAirport());
			if (previousDetectedStop != null) {
				previousDetectedStop.join(potentialStop);
			
			} else {
				potentialStops.put(potentialStop.getAirport(), potentialStop);
			}
		}
		
		return new ArrayList<>(potentialStops.values());
	}
	
	private Stop checkPotencialStop(RouteSearch search, RouteLocations route) {
		
		if (search.getDeparture().equals(route.getDepartureAirport()) 
			&& !search.getArrival().equals(route.getArrivalAirport())) {
			
			return Stop.builder()
					.airport(route.getArrivalAirport())
					.isArrival(Boolean.TRUE)
					.build();
		}
		
		if (!search.getDeparture().equals(route.getDepartureAirport()) 
			&& search.getArrival().equals(route.getArrivalAirport())) {
			
			return Stop.builder()
					.airport(route.getDepartureAirport())
					.isDeparture(Boolean.TRUE)
					.build();
		}
		
		return Stop.builder().build();
	}
	
	private List<String> filterInterconnectedStops(List<Stop> stops) {
		
		return stops.parallelStream()
				.filter(Stop::isInterconnected)
				.map(Stop::getAirport)
				.collect(Collectors.toList());
	}

}
