package com.ryanair.jmcr.service.routes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteSearch {

	private final String departure;
	private final String arrival;
}
