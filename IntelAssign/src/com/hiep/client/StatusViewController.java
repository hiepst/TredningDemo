package com.hiep.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.hiep.common.Room;
import com.hiep.common.util.UiUtil;
import com.hiep.server.RoomManager;

public class StatusViewController {

	private RoomManager roomManager;

	private StatusView view;

	public void init() {
		List<Room> rooms = new ArrayList<>(roomManager.getAll());

		view = new StatusView();
		view.setRooms(rooms);
		view.init();
		sizeAllColumnToFitData();
	}

	private void sizeAllColumnToFitData() {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					sizeAllColumnToFitData();
				}
			});
		}
		UiUtil.sizeAllColumnsToFitData(view.getTable(), true);
	}

	public int getTotalRooms() {
		return view.getRooms().size();
	}

	public List<Room> getRooms() {
		return view.getRooms();
	}

	public StatusView getView() {
		return view;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				showView();
			}
		});
	}

	private static void showView() {
		StatusViewController controller = new StatusViewController();
		controller.setRoomManager(new RoomManager());
		controller.init();

		JFrame frame = new JFrame("Intel Assignment");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		StatusView statusView = controller.getView();
		statusView.setPreferredSize(new Dimension(800, 650));

		frame.getContentPane().add(statusView, BorderLayout.CENTER);
		UiUtil.centerAndShow(frame);
	}

	public void setRoomManager(RoomManager roomManager) {
		this.roomManager = roomManager;
	}
}
