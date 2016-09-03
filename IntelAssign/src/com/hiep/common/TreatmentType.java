package com.hiep.common;

public enum TreatmentType {

	ISOLATION("Isolation", 6), MEDICATION_DRIP("Medicare Drip", 4), POST_SURGERY("Post Surgery",
			6), WOUND_CARE("Wound Care", 4), CHEST_TUBE("Chest Tube", 2);

	private String displayName;

	/**
	 * The more, the heavier the assignment is.
	 */
	private int score;

	public int getScore() {
		return score;
	}

	public String getDisplayName() {
		return displayName;
	}

	public TreatmentType fromString(String name) {
		for (TreatmentType type : TreatmentType.values()) {
			if (type.getDisplayName().equals(name)) {
				return type;
			}
		}

		return null;
	}

	private TreatmentType(String displayName, int score) {
		this.displayName = displayName;
		this.score = score;
	}

}
