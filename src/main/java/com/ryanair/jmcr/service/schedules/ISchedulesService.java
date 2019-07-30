package com.ryanair.jmcr.service.schedules;

import java.util.List;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.model.Schedule;
import com.ryanair.jmcr.service.dto.Flight;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;
import com.ryanair.jmcr.service.schedules.dto.ScheduleSearch;

public interface ISchedulesService {

	List<Flight> filterSchedules(FlightSearch flightSearch, List<Schedule> routeSchedules);

	List<Schedule> convert(List<ScheduleSearch> schedulesSearch, List<ScheduleAPI> schedulesAPI);
}
