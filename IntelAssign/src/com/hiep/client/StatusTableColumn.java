package com.hiep.client;

public enum StatusTableColumn {

	ROOM("Room"), PATIENT_LASTNAME("Patient Last Name"), PATIENT_TYPE("Patient Type"), POST_SURG("Post Surg"), MED_DRIP(
			"Med Drip"), CHEST_TUBE("Chest Tube"), WOUND_CARE("Wound Care"), ISOLATION("Isolation"), POSSIBLE_DC(
					"Possible DC"), NURSE_FIRSTNAME("Nurse First Name");

	private String displayName;

	public String getDisplayName() {
		return displayName;
	}

	public StatusTableColumn fromString(String name) {
		for (StatusTableColumn type : StatusTableColumn.values()) {
			if (type.getDisplayName().equals(name)) {
				return type;
			}
		}

		return null;
	}

	private StatusTableColumn(String displayName) {
		this.displayName = displayName;
	}

}
