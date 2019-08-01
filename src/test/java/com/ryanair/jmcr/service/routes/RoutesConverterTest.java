package com.ryanair.jmcr.service.routes;

import static com.ryanair.jmcr.TestHelper.DUB;
import static com.ryanair.jmcr.TestHelper.MAD;
import static com.ryanair.jmcr.TestHelper.RYANAIR;
import static com.ryanair.jmcr.TestHelper.WRO;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ryanair.jmcr.model.RouteLocations;
import com.ryanair.jmcr.service.routes.dto.RouteAPI;
import com.ryanair.jmcr.service.routes.impl.RoutesConverter;

public class RoutesConverterTest {

	RoutesConverter converter;
	
	List<RouteAPI> okRoutes;
	List<RouteAPI> koRoutes;
	
	RouteLocations okRouteLocation;
	RouteLocations koRouteLocation;
	
	@Before
	public void setUp() {
		
		converter = new RoutesConverter();
		
		okRoutes = List.of(
				new RouteAPI(DUB, WRO, null, RYANAIR),
				new RouteAPI(DUB, WRO, "", null),
				new RouteAPI(null, null, null, null),
				new RouteAPI(DUB, MAD, "", "NORWEGIAN"));
		
		koRoutes = new ArrayList<>();
		koRoutes.add(null);
	}

	@Test
	public void checkConverterTest() {
		
		assertThat(converter, notNullValue());
	}
	
	@Test
	public void convertTest() {
		
		for(RouteAPI route : okRoutes) {
			try {
				RouteLocations result = converter.convert(route);
				assertThat(result, notNullValue());
			
			} catch (Exception e) {
				fail(route.toString());
			}
		}
		
		try {
			converter.convert(koRoutes);
			fail("Null expected");
			
		} catch (NullPointerException e) {}
	}
	
	@Test
	public void convertListTest() {
		
		List<RouteLocations> result = converter.convert(okRoutes);
		assertThat(result.size(), is(okRoutes.size()));
	}
}
