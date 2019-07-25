package com.ryanair.jmcr.service.routes;

import java.util.List;

import com.ryanair.jmcr.model.Route;
import com.ryanair.jmcr.service.routes.dto.RouteAPI;

public interface RoutesService {

	List<Route> filter(List<RouteAPI> apiRoutes);
}
