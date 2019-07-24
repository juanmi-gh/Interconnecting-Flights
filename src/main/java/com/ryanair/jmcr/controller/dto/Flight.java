package com.ryanair.jmcr.controller.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Flight implements Serializable {

	private static final long serialVersionUID = -3262524826903699373L;

	private final Integer stops;
	private final List<Leg> legs;
	
}
