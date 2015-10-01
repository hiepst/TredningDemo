package cs.trend.dao;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import cs.trend.client.CassetteDataPoint;
import cs.trend.client.OutOfLimitListener;
import cs.trend.common.DataSourceUtil;

public class CsvDataSetDao implements DatasetDao {

	OutOfLimitListener outOfLimitListener;

	private static final int MAX_DATA_ITEMS = 1000;

	public static final double NOMINAL_COLD_2_TEMP = 21;

	public static final double NOMINAL_COLD_1_TEMP = 20.5;

	public static final double NOMINAL_HOT_1_SET_POINT = 30;

	public static final double NOMINAL_HOT_2_TEMP = 22.5;

	public static final double NOMINAL_HOT_1_TEMP = 23;

	private TimeSeriesCollection dataset = new TimeSeriesCollection();

	private Instant beginning = Instant.now();

	private CSVReader csvReader;

	private double[] hot1TempData;

	private double[] hot2TempData;

	private double[] cold1TempData;

	private double[] cold2TempData;

	private TimeSeries movingAverageSeries;

	private static int dataCount = 0;

	public static final int CAS1_HOT2_TEMP_INDEX = 66;

	public static final int CAS1_HOT1_TEMP_INDEX = 56;

	private int avgPeriodCount;

	private int avgSkip;

	public CsvDataSetDao(String fileName) {
		csvReader = new CSVReader(fileName);
		hot1TempData = csvReader.getDoubleData(CAS1_HOT1_TEMP_INDEX,
				MAX_DATA_ITEMS);
		hot2TempData = csvReader.getDoubleData(CAS1_HOT2_TEMP_INDEX,
				MAX_DATA_ITEMS);
	}

	@Override
	public void setOutOfLimitListener(OutOfLimitListener outOfLimitListener) {
		this.outOfLimitListener = outOfLimitListener;
	}

	@Override
	public TimeSeriesCollection getDataSet() {
		// addLimitSeries();

		return dataset;
	}

	@Override
	public TimeSeriesCollection getDataSet(String displayName) {
		// addLimitSeries();

		addSeries(null);

		return dataset;
	}

	@Override
	public void removeSeries(String displayName) {
		int seriesCount = dataset.getSeriesCount();
		if (seriesCount == 0) {
			return;
		}
		// dataset.removeSeries(seriesCount - 1);
		TimeSeries series = dataset.getSeries(displayName);
		dataset.removeSeries(series);
	}

	@Override
	public TimeSeries addSeries(String displayName) {
		TimeSeries series = new TimeSeries(displayName);
		dataset.addSeries(series);
		return series;
	}

	@Override
	public void addDatas() {
		Second now = new Second();
		System.out.println("Now = " + now.toString());
		for (Object obj : dataset.getSeries()) {
			TimeSeries s = (TimeSeries) obj;
			String displayName = s.getKey().toString();
			addData(displayName);
		}

	}

	@Override
	public void addData(String displayName) {
		CassetteDataPoint dataPoint = DataSourceUtil
				.getCassetteDataPoint(displayName);

		double value;
		TimeSeries timeSeries = dataset.getSeries(displayName);
		if (timeSeries == null) {
			return;
		}

		switch (dataPoint) {
		case COLD1_TEMPERATURE:
			value = NOMINAL_COLD_1_TEMP + getRandomDeviation();
			timeSeries.add(new Second(), value);
			break;

		case COLD2_TEMPERATURE:
			value = NOMINAL_COLD_2_TEMP + getRandomDeviation();
			timeSeries.add(new Second(), value);
			break;

		case COLD_SET_POINT:
			value = 5;
			timeSeries.add(new Second(), value);
			break;

		case HOT1_SET_POINT:
			value = NOMINAL_HOT_1_SET_POINT;
			timeSeries.add(new Second(), value);
			break;

		case HOT1_TEMPERATURE:
			value = hot1TempData[dataCount];
			System.out.println("Item count = " + dataCount + " - Value = "
					+ value);

			Random random = new Random();

			Instant now = Instant.now();
			long gap = ChronoUnit.SECONDS.between(beginning, now);
			if (gap % 120 == 0) {
				// trigger out of limit value
				value = NOMINAL_HOT_1_TEMP + 3;
				if (outOfLimitListener != null) {
					outOfLimitListener.outOfLimitPerform(now,
							now.plusMillis(2000), value);
				}
			}

			timeSeries.add(new Second(), value);
			break;

		case HOT2_TEMPERATURE:
			value = hot2TempData[dataCount];
			System.out.println("Item count = " + dataCount + " - Value = "
					+ value);

			timeSeries.add(new Second(), value);
			break;
		}

		dataCount++;
		if (dataCount == MAX_DATA_ITEMS) {
			// Reset to retate again.
			dataCount = 0;
		}
	}

	private double getRandomDeviation() {
		double dev = 0;
		Random random = new Random();

		long gap = ChronoUnit.SECONDS.between(beginning, Instant.now());
		if (gap % 30 == 0) { // every 30 seconds
			if (random.nextInt(100) < 50) {
				dev = -random.nextDouble();
			} else {
				dev = random.nextDouble();
			}
		} else if (gap % 80 == 0) {
			// trigger out of limit value
			if (random.nextInt(100) < 50) {
				dev = -3 + random.nextDouble();
			} else {
				dev = 3 + random.nextDouble();
			}
		}

		return dev;
	}

	@Override
	public void addMovingAverage(int periodCount, int skip) {
		// TODO Auto-generated method stub
		List series = dataset.getSeries();
		// Only support moving average for a single plot
		if (series.size() != 1) {
			return;
		}
		TimeSeries s = (TimeSeries) series.get(0);
		String name = "Moving Average";
		avgPeriodCount = periodCount;
		avgSkip = skip;
		movingAverageSeries = MovingAverage.createMovingAverage(s, name,
				periodCount, skip);
		dataset.addSeries(movingAverageSeries);
	}

	@Override
	public void removeMovingAverage() {
		// TODO Auto-generated method stub
		if (movingAverageSeries == null) {
			return;
		}
		dataset.removeSeries(movingAverageSeries);
	}

}
