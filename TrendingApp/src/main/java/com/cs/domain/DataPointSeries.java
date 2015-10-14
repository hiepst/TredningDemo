package com.cs.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataPointSeries {

	/**
	 * Data point name
	 */
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

	/**
	 * Values in this series
	 */
	private List<Double> values = new ArrayList<>();

	public DataPointSeries() {
		Random random = new Random();
		// Create random values for testing
		values = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			values.add(random.nextDouble() * 3 + 37);
		}

		// Trying to simulate delay due to large data set
		calculatePi(1000000);
	}

	private double calculatePi(int N) {
		double sum = 0.0; // final sum
		double term; // term without sign
		double sign = 1.0; // sign on each term
		for (int k = 0; k < N; k++) {
			term = 1.0 / (2.0 * k + 1.0);
			sum = sum + sign * term;
			if (k % (N / 50) == 0) // print one in 50
				System.out.println("k: " + k + ", " + sum + ", pi: " + sum * 4.0);
			sign = -sign;
		}
		System.out.println("Final pi/4 (approx., " + N + " terms): " + sum);
		System.out.println("Actual pi/4: " + Math.PI / 4.0);
		System.out.println("Final pi (approx., " + N + " terms): " + sum * 4.0);
		System.out.println("Actual pi: " + Math.PI);

		return sum;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("Name: " + name);
		sb.append(", start time: " + startTime);
		sb.append(", end time: " + endTime);
		sb.append(", values: " + values.toString());

		return sb.toString();
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public List<Double> getValues() {
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
