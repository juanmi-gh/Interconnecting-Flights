package com.ryanair.jmcr.service.schedules.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ryanair.jmcr.controller.dto.FlightSearch;
import com.ryanair.jmcr.model.FlightInfo;
import com.ryanair.jmcr.model.Schedule;
import com.ryanair.jmcr.service.schedules.ISchedulesConsumer;
import com.ryanair.jmcr.service.schedules.dto.ScheduleAPI;
import com.ryanair.jmcr.service.schedules.dto.ScheduleDayAPI;
import com.ryanair.jmcr.service.schedules.dto.ScheduleFlightAPI;
import com.ryanair.jmcr.service.schedules.dto.ScheduleSearch;

@Service
public class SchedulesConsumer implements ISchedulesConsumer {

	private static final String DEPARTURE_PLACEHOLDER = "{departure}";
	private static final String ARRIVAL_PLACEHOLDER = "{arrival}";
	private static final String YEAR_PLACEHOLDER = "{year}";
	private static final String MONTH_PLACEHOLDER = "{month}";

	@Autowired
	private RestTemplate restTemplate;

    @Value("${api.schedules}")
    private String schedulesPath;

    @Override
	public List<Schedule> findDirectFlights(FlightSearch flightSearch) {

    	List<Schedule> monthsSchedules = new ArrayList<>();

    	List<LocalDate> dates = calculateYearsAndMonths(flightSearch.getDepartureDateTime(), flightSearch.getArrivalDateTime());
    	for(LocalDate date : dates) {

    		ScheduleSearch scheduleSearch = ScheduleSearch.builder()
    			.departure(flightSearch.getDeparture())
    			.arrival(flightSearch.getArrival())
    			.year(String.valueOf(date.getYear()))
    			.month(String.valueOf(date.getMonthValue()))
    			.build();

    		String schedulePath = buildPath(scheduleSearch);
    		ScheduleAPI monthSchedules = sendRequest(schedulePath);
    		List<Schedule> schedules = convert(scheduleSearch, monthSchedules);

    		monthsSchedules.addAll(schedules);
    	}

    	return monthsSchedules;
	}

    private List<Schedule> convert(ScheduleSearch scheduleSearch, ScheduleAPI scheduleAPI) {
		
		List<Schedule> result = new ArrayList<>();
		
		Integer year = Integer.valueOf(scheduleSearch.getYear());
		Integer month = Integer.valueOf(scheduleSearch.getMonth());
		
		for(ScheduleDayAPI day: scheduleAPI.getDays()) {
			
			LocalDate date = LocalDate. of(year, month, day.getDay());			
			List<FlightInfo> flights = extractFlightsInfo(day);
			Schedule schedule = Schedule.builder()
										.date(date)
										.flights(flights)
										.build();
			
			result.add(schedule);
		}
		
		return result;
	}

	private List<FlightInfo> extractFlightsInfo(ScheduleDayAPI day) {
		
		List<FlightInfo> flights = new ArrayList<>();
		
		for(ScheduleFlightAPI flight : day.getFlights()) {
			
			LocalTime departure = LocalTime.parse(flight.getDepartureTime(), DateTimeFormatter.ISO_LOCAL_TIME);
			LocalTime arrival = LocalTime.parse(flight.getArrivalTime(), DateTimeFormatter.ISO_LOCAL_TIME);
			
			FlightInfo item = FlightInfo.builder()
										.departureTime(departure)
										.arrivalTime(arrival)
										.build();
			flights.add(item);
		}
		
		return flights;
	}

	@Override
	public List<ScheduleAPI> findFirstLeg(FlightSearch search, String stop) {

		List<ScheduleAPI> result = new ArrayList<>();
		
		ScheduleSearch scheduleSearch;
		
//		List<LocalDate> scheduleDates = calculateYearAndMonth();
//		for (LocalDate date: scheduleDates) {
//
//			scheduleSearch = ScheduleSearch.builder()
//						.departure(search.getDeparture())
//						.arrival(search.getArrival())
//						.departure(String.valueOf(date.getMonthValue()))
//						.arrival(String.valueOf(date.getYear()))
//						.build();
//		
//			String path = buildPath(scheduleSearch);
//			ScheduleAPI schedule = sendRequest(path);
//			result.add(schedule);
//		}
		
		return result;
	}

	@Override
	public List<ScheduleAPI> findSecondLeg(FlightSearch search, String stop) {
		return new ArrayList<>();
	}

	private List<LocalDate> calculateYearsAndMonths(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {

		List<LocalDate> result = new ArrayList<>();

		LocalDate departureDate = departureDateTime.toLocalDate().withDayOfMonth(1);
		LocalDate arrivalDate = arrivalDateTime.toLocalDate();

		while(departureDate.isBefore(arrivalDate) || departureDate.equals(arrivalDate)) {
			result.add(departureDate);
			departureDate = departureDate.plusMonths(1);
		}

		return result;
	}

	private String buildPath(ScheduleSearch search) {

		return schedulesPath
				.replace(DEPARTURE_PLACEHOLDER, search.getDeparture())
				.replace(ARRIVAL_PLACEHOLDER, search.getArrival())
				.replace(YEAR_PLACEHOLDER, search.getYear())
				.replace(MONTH_PLACEHOLDER, search.getMonth());
	}

	private ScheduleAPI sendRequest(String path) {

		ResponseEntity<ScheduleAPI> response = restTemplate.exchange(
				path,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<ScheduleAPI>(){});

		return response.getBody();
	}

}
