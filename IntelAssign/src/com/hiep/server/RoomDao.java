package com.hiep.server;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.hiep.common.Room;
import com.hiep.common.util.StringUtils;

public class RoomDao {
	public static final int TOTAL_ROOMS = 34;

	public List<Room> getAll() {
		List<Room> rooms = null;
		try {
			XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream(getPath().toString())));
			rooms = (List<Room>) decode.readObject();
			if (rooms == null) {
				return createRooms();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rooms = createRooms();
		}
		return rooms;
	}

	private List<Room> createRooms() {
		List<Room> rooms = new ArrayList<>();

		for (int i = 1; i <= TOTAL_ROOMS; i++) {
			Room r = new Room();
			r.setId(i);
			rooms.add(r);
		}
		return rooms;
	}

	public void saveAll(List<Room> rooms) {
		XMLEncoder e;
		try {
			Path path = getPath();
			e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path.toString())));
			e.writeObject(rooms);
			e.close();

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Path path = FileSystems.getDefault().getPath("/tmp/", "rooms.xml");
		//
		// try (BufferedWriter writer = Files.newBufferedWriter(path,
		// StandardOpenOption.CREATE)) {
		// for (Room r : rooms) {
		// String s = StringUtils.getAsXml(r);
		// writer.write(s, 0, s.length());
		// }
		//
		// } catch (IOException x) {
		// System.err.format("IOException: %s%n", x);
		// }
	}

	private Path getPath() {
		Path path = FileSystems.getDefault().getPath("/tmp/", "rooms.xml");
		return path;
	}

}
