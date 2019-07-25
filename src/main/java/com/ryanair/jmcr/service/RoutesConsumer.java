package com.ryanair.jmcr.service;

import java.util.List;

import com.ryanair.jmcr.service.dto.RouteAPI;

public interface RoutesConsumer {

	List<RouteAPI> collect();
}
