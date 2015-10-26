package com.cs.client.enumType;

public enum DurationValue {
	FIVE_MIN("5 Minutes", 5), TEN_MIN("10 Minutes", 10), FIFTEEN_MIN("15 Minutes", 15), THIRTY_MIN("30 Minutes",
			30), FORTY_FIVE_MIN("45 Minutes", 45), ONE_HR("1 Hour", 60), TWO_HR("2 Hours", 120), THREE_HR("3 Hours",
					180), FOUR_HR("4 Hours", 240), FIVE_HR("5 Hours", 300), SIX_HR("6 Hours", 360), SEVEN_HR("7 Hours",
							420), EIGHT_HR("8 Hours", 480), NINE_HR("9 Hours", 540), TEN_HR("10 Hours",
									600), ELVEN_HR("11 Hours", 660), ONE_DAY("1 Day", 1440), ONE_WEEK("1 Week",
											10080), TWO_WEEK("2 Weeks", 20160), THIRTY_DAY("30 Days", 43200);

	private String displayName;

	private int value;

	public int getValue() {
		return value;
	}

	private DurationValue(String displayName, int value) {
		this.displayName = displayName;
		this.value = value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.displayName;
	}

}
