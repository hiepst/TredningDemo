package cs.trend.client;

public enum CassetteDataPoint {
	HOT1_TEMPERATURE("Hot 1 Temp"), HOT2_TEMPERATURE("Hot 2 Temp"), COLD1_TEMPERATURE("Cold 1 Temp"), COLD2_TEMPERATURE(
			"Cold 2 Temp"), HOT1_SET_POINT("Hot 1 Set Point"), COLD_SET_POINT("Cold Set Point");

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
