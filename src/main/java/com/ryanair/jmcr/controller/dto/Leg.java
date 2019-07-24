package com.ryanair.jmcr.controller.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class Leg implements Serializable {

	private static final long serialVersionUID = -1378577773269353637L;

	private final String departureAirport;
	private final String arrivalAirport;
	private final String departureDateTime;
	private final String arrivalDateTime;
}
