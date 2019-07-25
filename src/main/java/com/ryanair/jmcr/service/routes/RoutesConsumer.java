package com.ryanair.jmcr.service.routes;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ryanair.jmcr.service.Consumer;
import com.ryanair.jmcr.service.routes.dto.RouteAPI;

@Service
public class RoutesConsumer implements Consumer<RouteAPI>{
	
    @Value("${api.routes}")
    private String routesPath;
	
	
	@Override
	public List<RouteAPI> collect() {

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<RouteAPI>> response = restTemplate.exchange(
				routesPath,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<RouteAPI>>(){});
		
		return response.getBody();	
	}

	

}
