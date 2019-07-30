package com.ryanair.jmcr.service.routes.impl;

import org.springframework.stereotype.Service;

import com.ryanair.jmcr.model.RouteLocations;
import com.ryanair.jmcr.service.Converter;
import com.ryanair.jmcr.service.routes.dto.RouteAPI;

@Service
public class RoutesConverter implements Converter<RouteLocations, RouteAPI> {
	
	@Override
	public RouteLocations convert(RouteAPI item) {

		return RouteLocations.builder()
				.departureAirport(item.getAirportFrom())
				.arrivalAirport(item.getAirportTo())
				.build();
	}

}
