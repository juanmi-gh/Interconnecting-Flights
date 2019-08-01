package com.ryanair.jmcr.service.routes.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteAPI implements Serializable {
	
	private static final long serialVersionUID = -2303113209118358371L;

	private String airportFrom;
	private String airportTo;
	private String connectingAirport;
	private String operator;

}
