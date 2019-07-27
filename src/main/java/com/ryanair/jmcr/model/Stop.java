package com.ryanair.jmcr.model;

import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
public class Stop {

	private String airport;
	
	@Default
	private Boolean isDeparture = Boolean.FALSE;	
	
	@Default
	private Boolean isArrival = Boolean.FALSE;
	
	public Boolean isEmpty() {
		return airport.isEmpty();
	}
	
	public void join(Stop stop) {
		
		this.isDeparture = this.isDeparture || stop.isDeparture;
		this.isArrival = this.isArrival || stop.isArrival;
	}
	
	public Boolean isInterconnected() {
		return isDeparture && isArrival;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stop other = (Stop) obj;
		if (airport == null) {
			if (other.airport != null)
				return false;
		} else if (!airport.equals(other.airport)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((airport == null) ? 0 : airport.hashCode());
		return result;
	}
	
	
}
