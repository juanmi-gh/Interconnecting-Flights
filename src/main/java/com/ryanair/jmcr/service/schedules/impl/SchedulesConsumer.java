package com.ryanair.jmcr.service.schedules.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ryanair.jmcr.service.schedules.ISchedulesConsumer;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;
import com.ryanair.jmcr.service.schedules.dto.ScheduleSearch;

@Service
public class SchedulesConsumer implements ISchedulesConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger("SCHEDULES-CONSUMER");
	
	private static final String DEPARTURE_PLACEHOLDER = "{departure}";
	private static final String ARRIVAL_PLACEHOLDER = "{arrival}";
	private static final String YEAR_PLACEHOLDER = "{year}";
	private static final String MONTH_PLACEHOLDER = "{month}";

	@Autowired
	private RestTemplate restTemplate;

    @Value("${api.schedules}")
    private String schedulesPath;

    @Override
	public List<ScheduleAPI> findSchedules(List<ScheduleSearch> schedulesSearch) {

    	List<ScheduleAPI> schedules = new ArrayList<>();

    	for(ScheduleSearch scheduleSearch : schedulesSearch) {

    		String schedulePath = buildPath(scheduleSearch);
    		ScheduleAPI monthSchedules = sendRequest(schedulePath);
    		schedules.add(monthSchedules);
    	}

    	return schedules;
	}
    
    private String buildPath(ScheduleSearch search) {

		return schedulesPath
				.replace(DEPARTURE_PLACEHOLDER, search.getDeparture())
				.replace(ARRIVAL_PLACEHOLDER, search.getArrival())
				.replace(YEAR_PLACEHOLDER, search.getYear())
				.replace(MONTH_PLACEHOLDER, search.getMonth());
	}

	private ScheduleAPI sendRequest(String path) {

		try {
    		LOGGER.info("Connecting {}", path);
    		ResponseEntity<ScheduleAPI> response = restTemplate.exchange(
    				path,
    				HttpMethod.GET,
    				null,
    				new ParameterizedTypeReference<ScheduleAPI>(){});
    
    		return response.getBody();

		} catch (HttpClientErrorException e) {
			LOGGER.warn(e.getMessage());
			return new ScheduleAPI();
		}
	}

}
