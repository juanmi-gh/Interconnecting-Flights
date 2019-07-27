package com.ryanair.jmcr.service.schedules.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.service.schedules.ISchedulesConsumer;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;

@Service
public class SchedulesConsumer implements ISchedulesConsumer {

	@Autowired
	private RestTemplate restTemplate;
	
    @Value("${api.schedules}")
    private String schedulesPath;


	@Override
	public List<ScheduleAPI> findFirstLeg(FlightSearch search, String stop) {
		return sendRequest("");
	}


	@Override
	public List<ScheduleAPI> findSecondLeg(FlightSearch search, String stop) {
		return sendRequest("");
	}
	
	private List<ScheduleAPI> sendRequest(String path) {

		ResponseEntity<List<ScheduleAPI>> response = restTemplate.exchange(
				schedulesPath,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<ScheduleAPI>>(){});
		
		return response.getBody();
	}

}
