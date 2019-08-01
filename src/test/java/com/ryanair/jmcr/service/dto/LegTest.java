package com.ryanair.jmcr.service.dto;

import static com.ryanair.jmcr.TestHelper.DUB;
import static com.ryanair.jmcr.TestHelper.FOURTEEN_OCLOCK;
import static com.ryanair.jmcr.TestHelper.MAD;
import static com.ryanair.jmcr.TestHelper.NINE_OCLOCK;
import static com.ryanair.jmcr.TestHelper.ONE_MIN_TO_16_OCLOCK;
import static com.ryanair.jmcr.TestHelper.SIXTEEN_OCLOCK;
import static com.ryanair.jmcr.TestHelper.TWENTY_OCLOCK;
import static com.ryanair.jmcr.TestHelper.WRO;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;;

public class LegTest {

	Leg departureLeg;
	Leg koArrivalLeg;
	Leg okArrivalLeg;
	
	@Before
	public void setUp() {
		departureLeg = Leg.builder()
							.departureAirport(DUB)
							.arrivalAirport(MAD)
							.departureDateTime(NINE_OCLOCK)
							.arrivalDateTime(FOURTEEN_OCLOCK)
							.build();
		
		koArrivalLeg = Leg.builder()
            				.departureAirport(MAD)
            				.arrivalAirport(WRO)
            				.departureDateTime(ONE_MIN_TO_16_OCLOCK)
            				.arrivalDateTime(SIXTEEN_OCLOCK)
            				.build();
		
		okArrivalLeg = Leg.builder()
				.departureAirport(MAD)
				.arrivalAirport(WRO)
				.departureDateTime(SIXTEEN_OCLOCK)
				.arrivalDateTime(TWENTY_OCLOCK)
				.build();
	}
	
	@Test
	public void isValidConnectionTest() {
		
		assertThat(departureLeg.isValidConnection(okArrivalLeg), is(true));
		assertThat(departureLeg.isValidConnection(koArrivalLeg), is(false));
	}
}
