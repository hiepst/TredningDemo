package com.cs.client.enumType;

public enum Locker {
	FLIGHT("Flight"), GROUND("Ground");
	private String displayName;

	private Locker(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return this.displayName;
	}

}
