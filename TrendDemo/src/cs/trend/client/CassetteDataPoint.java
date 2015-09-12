package cs.trend.client;

public enum CassetteDataPoint {
	HOT_1_TEMP("Hot 1 Temp"), HOT_2_TEMP("Hot 2 Temp"), COLD_1_TEMP(
			"Cold 1 Temp"), COLD_2_TEMP("Cold 2 Temp"), HOT_1_SET_POINT(
			"Hot 1 Set Point"), COLD_SET_POINT("Cold Set Point");

	// "Power Cycles", "Humidity,", "Flow Rate", "Current",
	// "Power", "Inlet Fan RPM", "Outlet Fan RPM", "Hot 1 Set Point",
	// "Cold Set Point", "Current Line", "Gas Purge Time",
	// "Gas Purge Interval" });

	private String displayName;

	private CassetteDataPoint(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.displayName;
	}

}
