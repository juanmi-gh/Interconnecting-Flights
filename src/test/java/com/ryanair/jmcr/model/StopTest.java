package com.ryanair.jmcr.model;

import static com.ryanair.jmcr.TestHelper.MAD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class StopTest {

	Stop okStop;
	Stop okStop2;
	Stop koStop;
	Stop koStop2;
	
	@Before
	public void setUp() {
		okStop = Stop.builder()
				.airport(MAD)
				.isDeparture(true)
				.isArrival(true)
				.build();

        okStop2 = Stop.builder()
        	.isDeparture(true)
        	.isArrival(true)
        	.build();
        
        koStop = Stop.builder().build();
        koStop2 = Stop.builder()
        	.airport(MAD)
        	.isDeparture(true)
        	.build();	
	}
	
	@Test
	public void joinTest() {
		
		koStop.join(okStop);
		assertThat(koStop.getIsDeparture(), is(true));
		assertThat(koStop.getIsArrival(), is(true));
	}
	
	@Test
	public void joinTest2() {
		koStop.join(okStop2);
		assertThat(koStop.getIsDeparture(), is(true));
		assertThat(koStop.getIsArrival(), is(true));
	}
	
	@Test
	public void joinTest3() {
		koStop.join(koStop2);
		assertThat(koStop.getIsDeparture(), is(true));
		assertThat(koStop.getIsArrival(), is(false));
	}
	
	@Test
	public void isInterconnectedTest() {
		assertThat(okStop.isInterconnected(), is(true));
		assertThat(okStop2.isInterconnected(), is(true));
		assertThat(koStop.isInterconnected(), is(false));
		assertThat(koStop2.isInterconnected(), is(false));		
	}
}
