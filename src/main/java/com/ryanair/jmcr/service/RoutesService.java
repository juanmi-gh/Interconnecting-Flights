package com.ryanair.jmcr.service;

import java.util.List;

import com.ryanair.jmcr.model.Route;
import com.ryanair.jmcr.service.dto.RouteAPI;

public interface RoutesService {

	List<Route> filter(List<RouteAPI> apiRoutes);
}
