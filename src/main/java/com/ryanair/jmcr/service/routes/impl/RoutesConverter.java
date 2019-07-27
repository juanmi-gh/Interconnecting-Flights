package com.ryanair.jmcr.service.routes.impl;

import java.util.List;
import java.util.stream.Collectors;

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
	
	@Override
	public List<RouteLocations> convert(List<RouteAPI> apiRoutes) {

		RoutesConverter converter = this;
		
		return apiRoutes.stream()
				.map(converter::convert)
				.collect(Collectors.toList());
	}

}
