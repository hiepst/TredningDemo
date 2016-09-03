package com.hiep.common;

public class Room {

	private int id;

	private String patientLastName;

	private PatientType patientType = PatientType.NONE;

	private boolean postSurg;

	private boolean medDrip;

	private boolean chestTube;

	private boolean woundCare;

	private boolean isolation;

	private boolean possibleDc;

	private String nurse;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PatientType getPatientType() {
		return patientType;
	}

	public void setPatientType(PatientType patientType) {
		this.patientType = patientType;
	}

	public boolean isPostSurg() {
		return postSurg;
	}

	public void setPostSurg(boolean postSurg) {
		this.postSurg = postSurg;
	}

	public boolean isMedDrip() {
		return medDrip;
	}

	public void setMedDrip(boolean medDrip) {
		this.medDrip = medDrip;
	}

	public boolean isChestTube() {
		return chestTube;
	}

	public void setChestTube(boolean chestTube) {
		this.chestTube = chestTube;
	}

	public boolean isWoundCare() {
		return woundCare;
	}

	public void setWoundCare(boolean woundCare) {
		this.woundCare = woundCare;
	}

	public boolean isIsolation() {
		return isolation;
	}

	public void setIsolation(boolean isolation) {
		this.isolation = isolation;
	}

	public boolean isPossibleDc() {
		return possibleDc;
	}

	public void setPossibleDc(boolean possibleDc) {
		this.possibleDc = possibleDc;
	}

	public String getPatientLastName() {
		return patientLastName;
	}

	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}

	public String getNurse() {
		return nurse;
	}

	public void setNurse(String nurse) {
		this.nurse = nurse;
	}

}
