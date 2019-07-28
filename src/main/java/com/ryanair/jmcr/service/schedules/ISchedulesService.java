package com.ryanair.jmcr.service.schedules;

import java.util.List;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.model.Schedule;
import com.ryanair.jmcr.service.dto.Flight;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;

public interface ISchedulesService {

	List<Flight> filterSchedules(FlightSearch flightSearch, List<Schedule> routeSchedules);
	
	List<Flight> matchConnections(List<ScheduleAPI> firstLegSchedules, List<ScheduleAPI> secondLegSchedules);
}
