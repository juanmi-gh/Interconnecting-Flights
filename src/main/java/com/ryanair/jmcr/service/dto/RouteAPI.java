package com.ryanair.jmcr.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteAPI implements Serializable {
	
	private static final long serialVersionUID = -2303113209118358371L;

	private final String airportFrom;
	private final String airportTo;
	private final String connectingAirport;
	private final String operator;

}
