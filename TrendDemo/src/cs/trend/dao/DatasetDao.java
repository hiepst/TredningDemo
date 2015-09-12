package cs.trend.dao;

import org.jfree.data.time.TimeSeriesCollection;

import cs.trend.demo.CassetteDataPoint;
import cs.trend.demo.OutOfLimitListener;
import cs.trend.demo.Source;

public interface DatasetDao {

	TimeSeriesCollection getDataSet();

	TimeSeriesCollection getDataSet(Source source, CassetteDataPoint dataPoint);

	void removeSeries();

	void addSeries(Source source, CassetteDataPoint dataPoint);

	void addDatas();

	void addData(Source source, CassetteDataPoint dataPoint);

	void setOutOfLimitListener(OutOfLimitListener outOfLimitListener);

	void addMovingAverage(int periodCount, int skip);
	
	void removeMovingAverage();
}
