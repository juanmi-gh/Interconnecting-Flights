package com.ryanair.jmcr.service.routes;

import java.util.List;

import com.ryanair.jmcr.service.routes.dto.RouteAPI;
import com.ryanair.jmcr.service.routes.dto.RouteSearch;

public interface IRoutesService {

	List<String> findStopLocations(List<RouteAPI> apiRoutes, RouteSearch search);
}
