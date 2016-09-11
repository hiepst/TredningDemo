package com.hiep.server;

import java.util.List;

import com.hiep.common.Room;

public class RoomManager {

	private RoomDao dao;

	public RoomManager() {
		super();
		dao = new RoomDao();
	}

	public List<Room> getAll() {
		return dao.getAll();
	}

	public void saveAll(List<Room> rooms) {
		dao.saveAll(rooms);
	}
}
