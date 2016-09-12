package com.hiep.common;

import java.util.List;

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

	private String nurseFirstName;

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

	public String getNurseFirstName() {
		return nurseFirstName;
	}

	public void setNurseFirstName(String nurseFirstName) {
		this.nurseFirstName = nurseFirstName;
	}

	public boolean hasPatientWithTheSameLastName(List<Room> rooms) {
		for (Room r : rooms) {
			if (id == r.getId()) {
				continue;
			}
			if (patientLastName.equalsIgnoreCase(r.getPatientLastName())) {
				return true;
			}
		}

		return false;
	}

	public boolean isWithinAcceptableDistance(List<Room> rooms) {
		for (Room r : rooms) {
			if (id == r.getId()) {
				continue;
			}
			if (patientLastName.equalsIgnoreCase(r.getPatientLastName())) {
				return true;
			}
		}

		return false;
	}

}
