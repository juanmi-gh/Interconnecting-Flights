package com.ryanair.jmcr.service.routes;

import java.util.List;

import com.ryanair.jmcr.service.routes.dto.RouteAPI;

public interface IRoutesConsumer {

	List<RouteAPI> collect();
}
