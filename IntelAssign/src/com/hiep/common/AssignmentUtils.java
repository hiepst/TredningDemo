package com.hiep.common;

import java.util.List;

/**
 * 
 * @author hiepst
 *
 */
public class AssignmentUtils {

	public static boolean canGroup(List<Room> rooms) {
		if (rooms.size() > 4) {
			throw new RuntimeException("A group cannot have more than 4 rooms/patients.");
		}

		return false;
	}
}
