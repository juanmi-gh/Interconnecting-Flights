package com.ryanair.jmcr.service;

import java.util.List;

public interface Consumer<T> {

	List<T> collect();
}
