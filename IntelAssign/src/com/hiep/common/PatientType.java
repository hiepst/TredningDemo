package com.hiep.common;

public enum PatientType {

	TOTAL_CARE("Total Care", 10), PARTIAL_CARE("Partial Care", 8), INDEPENDENT("Idependent", 1), NONE("None", 0);

	private String displayName;

	/**
	 * The more, the heavier the assignment is.
	 */
	private int scale;

	public int getScore() {
		return scale;
	}

	public String getDisplayName() {
		return displayName;
	}

	public PatientType fromString(String name) {
		for (PatientType type : PatientType.values()) {
			if (type.getDisplayName().equals(name)) {
				return type;
			}
		}

		return null;
	}

	private PatientType(String displayName, int scale) {
		this.displayName = displayName;
		this.scale = scale;
	}

}
