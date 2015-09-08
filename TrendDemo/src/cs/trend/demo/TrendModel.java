package cs.trend.demo;

import java.awt.Color;
import java.util.Date;

/**
 * Trending model that keeps all the configurations required to plot real time
 * or archived telemetry data.
 * 
 * @author hvhong
 *
 */
public class TrendModel {
	private String parameterName;
	private Date startTime;
	private Date endTime;
	private Color lineColor;
	private float highUpperLimit;
	private float lowUpperLimit;
	private float highLowerLimit;
	private float lowlowerLimit;
	
	private int realtimeRangeInHour;
	
}
