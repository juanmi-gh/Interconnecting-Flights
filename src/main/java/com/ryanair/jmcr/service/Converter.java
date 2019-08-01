package com.ryanair.jmcr.service;

import java.util.List;
import java.util.stream.Collectors;

public interface Converter<T, E> {

	T convert(E item);
	
	default List<T> convert(List<E> list) {
		
		return list.parallelStream()
				.map(this::convert)
				.collect(Collectors.toList());
	}
}
