package com.hiep.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.hiep.common.Room;
import com.hiep.common.util.UiUtil;

public class StatusViewController {

	private static final int TOTAL_ROOMS = 34;
	private List<Room> rooms;

	private StatusView view;

	public void init() {
		rooms = new ArrayList<>();

		for (int i = 1; i <= TOTAL_ROOMS; i++) {
			Room r = new Room();
			r.setId(i);
			rooms.add(r);
		}

		view = new StatusView();
		view.setController(this);
		view.init();
		sizeAllColumnToFitData();
	}

	private void sizeAllColumnToFitData() {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					sizeAllColumnToFitData();
				}
			});
		}
		UiUtil.sizeAllColumnsToFitData(view.getTable(), true);
	}

	public int getTotalRooms() {
		return rooms.size();
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public StatusView getView() {
		return view;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				showView();
			}
		});
	}

	private static void showView() {
		StatusViewController controller = new StatusViewController();
		controller.init();

		JFrame frame = new JFrame("Intel Assignment");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		StatusView statusView = controller.getView();
		statusView.setPreferredSize(new Dimension(800, 650));

		frame.getContentPane().add(statusView, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
