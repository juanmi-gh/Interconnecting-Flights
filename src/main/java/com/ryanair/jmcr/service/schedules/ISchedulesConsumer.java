package com.ryanair.jmcr.service.schedules;

import java.util.List;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;

public interface ISchedulesConsumer {

	List<ScheduleAPI> findFirstLeg(FlightSearch search, String stop);
	
	List<ScheduleAPI> findSecondLeg(FlightSearch search, String stop);
}
