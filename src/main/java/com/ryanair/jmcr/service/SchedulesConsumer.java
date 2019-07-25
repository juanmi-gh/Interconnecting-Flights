package com.ryanair.jmcr.service;

import java.util.List;

import com.ryanair.jmcr.model.Route;
import com.ryanair.jmcr.service.dto.ScheduleAPI;

public interface SchedulesConsumer {

	List<ScheduleAPI> collectByRoutes(List<Route> routes);
}
