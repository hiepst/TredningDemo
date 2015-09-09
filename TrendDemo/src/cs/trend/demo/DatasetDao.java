package cs.trend.demo;

import org.jfree.data.time.TimeSeriesCollection;

public interface DatasetDao {

	TimeSeriesCollection getDataSet();

	TimeSeriesCollection getDataSet(Source source, CassetteDataPoint dataPoint);

	void removeSeries();

	void addSeries(Source source, CassetteDataPoint dataPoint);

	void addDatas();

	void addData(Source source, CassetteDataPoint dataPoint);

	void setOutOfLimitListener(OutOfLimitListener outOfLimitListener);
}
