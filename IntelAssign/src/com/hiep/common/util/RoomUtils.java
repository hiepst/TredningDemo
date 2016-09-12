package com.hiep.common.util;

import java.util.List;

import com.hiep.common.PatientType;
import com.hiep.common.Room;

public class RoomUtils {

	public static boolean hasTwoPatientsWithSameLastName(List<Room> rooms) {
		for (Room r : rooms) {
			if (r.hasPatientWithTheSameLastName(rooms)) {
				return true;
			}
		}

		return false;
	}

	public static boolean hasMoreThanOnePossibleDc(List<Room> rooms) {
		int count = 0;
		for (Room r : rooms) {
			if (r.isPossibleDc()) {
				count++;
			}
		}

		return count > 1;
	}

	public static boolean hasMoreThanOneMedDrip(List<Room> rooms) {
		int count = 0;
		for (Room r : rooms) {
			if (r.isMedDrip()) {
				count++;
			}
		}

		return count > 1;
	}

	public static boolean hasMoreThanOnePostSurg(List<Room> rooms) {
		int count = 0;
		for (Room r : rooms) {
			if (r.isPostSurg()) {
				count++;
			}
		}

		return count > 1;
	}

	public static boolean hasMoreThanTwoTotalCare(List<Room> rooms) {
		int count = 0;
		for (Room r : rooms) {
			if (r.getPatientType().equals(PatientType.TOTAL_CARE)) {
				count++;
			}
		}

		return count > 2;
	}

	public static boolean hasMoreThanTwoIsolation(List<Room> rooms) {
		int count = 0;
		for (Room r : rooms) {
			if (r.isIsolation()) {
				count++;
			}
		}

		return count > 2;
	}
}
