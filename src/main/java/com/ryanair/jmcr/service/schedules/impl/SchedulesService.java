package com.ryanair.jmcr.service.schedules.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ryanair.jmcr.service.dto.Flight;
import com.ryanair.jmcr.service.schedules.ISchedulesService;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;

@Service
public class SchedulesService implements ISchedulesService {

	@Override
	public List<Flight> matchConnections(List<ScheduleAPI> firstLegSchedules, List<ScheduleAPI> secondLegSchedules) {
		// TODO Auto-generated method stub
		return null;
	}

}
