package com.ryanair.jmcr.service.schedules;

import java.util.List;

import com.ryanair.jmcr.service.dto.Flight;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;

public interface ISchedulesService {

	List<Flight> matchConnections(List<ScheduleAPI> firstLegSchedules, List<ScheduleAPI> secondLegSchedules);
}
