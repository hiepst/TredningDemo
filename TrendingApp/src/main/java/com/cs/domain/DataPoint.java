package com.cs.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataPoint {
	private String name;

	/**
	 * Start time of the series. Text representation of a LocalDateTime
	 * instance. Format should be in the default form "yyyy-mm-ddThh:mm:ss".
	 */
	private String startTime;

	/**
	 * End time of the series. Text representation of a LocalDateTime instance.
	 * Format should be in the default form "yyyy-mm-ddThh:mm:ss".
	 */
	private String endTime;

	/**
	 * Each data point is separated by this interval in second(s). By default,
	 * it is 1 second.
	 */
	private int interval = 1;

	private List<Double> values;

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public List<Double> getValues() {
		Random random = new Random();
		// Create random values for testing
		values = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			values.add(random.nextDouble() * 3 + 37);
		}
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartTime() {
		return startTime;
	}

	/**
	 * Get a LocalDateTime instance of the start time.
	 * 
	 * @return
	 */
	public LocalDateTime getLocalStartTime() {
		return LocalDateTime.parse(startTime);
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	/**
	 * Get a LocalDateTime instance of the end time.
	 * 
	 * @return
	 */
	public LocalDateTime getLocalEndTime() {
		return LocalDateTime.parse(endTime);
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
