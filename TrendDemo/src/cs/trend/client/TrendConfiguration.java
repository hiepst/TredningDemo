package cs.trend.client;

/**
 * This class stores a runtime trending configuration that can be saved to a
 * *.trcf file and reopened at a later time. Common trending configuration file
 * can be shared among operators at different workstations to improve
 * productivity.
 * 
 * @author hvhong
 *
 */
public class TrendConfiguration {

	private String source;

	private String dataPoint;

	private String startTimestamp;

	private String endTimestamp;

	private int durationMinutes;

	private boolean showLimits;

	private int lineColorRGB;

}
