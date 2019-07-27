package com.ryanair.jmcr.service;

import java.util.List;

public interface Converter<T, E> {

	T convert(E item);
	
	List<T> convert(List<E> list);
}
