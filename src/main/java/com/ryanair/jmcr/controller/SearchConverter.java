package com.ryanair.jmcr.controller;

import org.springframework.stereotype.Service;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.service.Converter;
import com.ryanair.jmcr.service.routes.dto.RouteSearch;

@Service
public class SearchConverter implements Converter<RouteSearch, FlightSearch> {

	@Override
	public RouteSearch convert(FlightSearch item) {

		return RouteSearch.builder()
			.departure(item.getDeparture())
			.arrival(item.getArrival())
			.build();
	}

}
