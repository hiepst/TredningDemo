package cs.trend.demo;

import org.jfree.data.time.TimeSeriesCollection;

public interface DatasetDao {

	TimeSeriesCollection getDataSet();

	TimeSeriesCollection getDataSet(CassetteDataPoint dataPoint);

	void removeSeries();

	void addSeries(CassetteDataPoint dataPoint);

	void addDatas();

	void addData(CassetteDataPoint dataPoint);

	void setOutOfLimitListener(OutOfLimitListener outOfLimitListener);
}
