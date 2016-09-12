package com.hiep.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hiep.common.Room;

public class RoomManager {

	private RoomDao dao;

	private Map<Integer, List<Integer>> acceptableDistantRooms;

	public RoomManager() {
		super();
		dao = new RoomDao();
		acceptableDistantRooms = new HashMap<Integer, List<Integer>>();
		for (int room = 1; room <= dao.TOTAL_ROOMS; room++) {
			switch (room) {
			case 1:
				acceptableDistantRooms.put(1, Arrays.asList(26, 27, 28, 29, 30, 31, 32, 33, 34, 2, 3, 4, 5, 6));
				break;
			case 2:
				acceptableDistantRooms.put(2, Arrays.asList(26, 27, 28, 29, 30, 31, 32, 33, 34, 1, 3, 4, 5, 6));
				break;
			case 3:
				acceptableDistantRooms.put(3, Arrays.asList(26, 27, 28, 29, 30, 31, 32, 33, 34, 1, 2, 4, 5, 6));
				break;
			case 4:
				acceptableDistantRooms.put(4, Arrays.asList(22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 1, 2, 3,
						5, 6, 7, 8, 9, 10, 11));
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
			case 12:
				break;
			case 13:
				break;
			case 14:
				break;
			case 15:
				break;
			case 16:
				break;
			case 17:
				break;
			case 18:
				break;
			case 19:
				break;
			case 20:
				break;
			case 21:
				break;
			case 22:
				break;
			case 23:
				break;
			case 24:
				break;
			case 25:
				break;
			case 26:
				break;
			case 27:
				break;
			case 28:
				break;
			case 29:
				break;
			case 30:
				break;
			case 31:
				break;
			case 32:
				break;
			case 33:
				break;
			case 34:
				break;
			}
		}
	}

	public List<Room> getAll() {
		return dao.getAll();
	}

	public void saveAll(List<Room> rooms) {
		dao.saveAll(rooms);
	}
}
