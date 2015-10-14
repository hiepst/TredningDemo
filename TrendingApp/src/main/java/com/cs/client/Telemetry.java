package com.cs.client;

import java.io.Serializable;

public interface Telemetry<T> extends Serializable {

	/**
	 * Get data set that contains series of all the specified point names.
	 * Depending the specified time range, data resolution can be full at one
	 * data point per second or thin at a variable rate that satisfied the
	 * maximum number of data points can be return per call.
	 * 
	 * TODO TO BE ADJUSTED - The experimental threshold for full resolution now
	 * is 864,0000 data points per method call. That is the total of 10 point
	 * names each with 24 hours of per second data, or 5 point names each with 2
	 * days of per second data, or 1 point name with 10 days of per second data.
	 * 
	 * 864,000 = 60 seconds X 60 minutes X 24 hours X 10 point names
	 * 
	 * 
	 * @param startTimestamp
	 *            The start timestamp for data set. If null, the earliest
	 *            available timestamp is used
	 * @param endTimestamp
	 *            The end timestamp for data set. If null, the latest available
	 *            timestamp is used
	 * @param includeAverage
	 *            Whether to include the average series data all the point names
	 *            in this method. True to include, otherwise false
	 * @param includeDifference
	 *            Whether to include the difference series for all the point
	 *            names in this method. True to include, otherwise false
	 * @param pointNames
	 *            The array of point names
	 * @return The data set that contains series of all the point names
	 */
	T getDataSet(String startTimestamp, String endTimestamp, boolean includeAverage, boolean includeDifference,
			String... pointNames);
}
