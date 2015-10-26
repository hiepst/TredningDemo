package com.cs.client.enumType;

public enum CassetteDataPoint {
	HOT1_TEMP("Hot 1 Temp"), HOT2_TEMP("Hot 2 Temp"), COLD1_TEMP("Cold 1 Temp"), COLD2_TEMP("Cold 2 Temp"), HUMIDITY(
			"Humidity"), PUMP_CURRENT("Pump Current");

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
