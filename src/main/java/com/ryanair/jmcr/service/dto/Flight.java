package com.ryanair.jmcr.service.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Flight implements Serializable {

	private static final long serialVersionUID = -3262524826903699373L;

	private final Integer stops;
	private final List<Leg> legs;
	
}
