package com.ryanair.jmcr.service.schedules;

import java.util.List;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;
import com.ryanair.jmcr.service.schedules.dto.ScheduleSearch;

public interface ISchedulesConsumer {

	List<ScheduleAPI> findSchedules(List<ScheduleSearch> schedulesSearch);
	

}
