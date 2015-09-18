package cs.trend.dao;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import cs.trend.client.CassetteDataPoint;
import cs.trend.client.OutOfLimitListener;
import cs.trend.client.Source;

public interface DatasetDao {

	TimeSeriesCollection getDataSet();

	TimeSeriesCollection getDataSet(Source source, CassetteDataPoint dataPoint);

	void removeSeries();

	TimeSeries addSeries(Source source, CassetteDataPoint dataPoint);

	void addDatas();

	void addData(Source source, CassetteDataPoint dataPoint);

	void setOutOfLimitListener(OutOfLimitListener outOfLimitListener);

	void addMovingAverage(int periodCount, int skip);
	
	void removeMovingAverage();
}
