package com.hiep.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.hiep.common.PatientType;
import com.hiep.common.Room;
import com.hiep.common.util.RoomUtils;
import com.hiep.common.util.StringUtils;
import com.hiep.common.util.UiUtil;
import com.hiep.server.RoomManager;

public class ApplicationController {
	private RoomManager roomManager;
	private StatusViewController statusViewController;
	private ApplicationView applicationView;
	private JFrame appFrame;

	public void init() {
		roomManager = new RoomManager();

		statusViewController = new StatusViewController();
		statusViewController.setRoomManager(roomManager);
		statusViewController.init();

		applicationView = new ApplicationView();
		applicationView.setPreferredSize(new Dimension(1000, 800));
		applicationView.setMainPanel(statusViewController.getView());
		applicationView.init();

		applicationView.getGenerateAssignmentButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// if (!dataCompleted()) {
				// return;
				// }
				saveViewData();
				showAssignments();
			}

		});

		applicationView.getExitMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveViewData();
				appFrame.dispose();
			}
		});

		applicationView.getAboutMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(appFrame, "Copy Rights \u00a9 Hiep Hong\nAll Rights Reserved.",
						"About Intel Assignment Application", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				showApplicationView();
			}
		});

	}

	private void showAssignments() {
		int totalNurses = statusViewController.getTotalNurses();
		List<Room> occupiedRooms = statusViewController.getOccupiedRooms();
		int totalGroups = (int) Math.ceil(occupiedRooms.size() / totalNurses);
		if (totalGroups < 1) {
			totalGroups = 1;
		}

		Map<String, List<Room>> nurseToRooms = new HashMap<>();
		for (Room r : occupiedRooms) {
			String key = r.getNurseFirstName();
			if (nurseToRooms.get(key) == null) {
				nurseToRooms.put(key, new ArrayList<>());
			}
			nurseToRooms.get(key).add(r);
		}

		Map<String, List<Room>> assignments = new HashMap<>();
		for (String key : nurseToRooms.keySet()) {
			List<Room> rooms = nurseToRooms.get(key);
			// Criteria 1: no more than two patients with the same last
			// name.
			if (RoomUtils.hasTwoPatientsWithSameLastName(rooms)) {
				continue;
			}
			// Criteria 2: an assignment shall not have more than one
			// possible DC.
			if (RoomUtils.hasMoreThanOnePossibleDc(rooms)) {
				continue;
			}
			if (RoomUtils.hasMoreThanOneMedDrip(rooms)) {
				continue;
			}
			if (RoomUtils.hasMoreThanOnePostSurg(rooms)) {
				continue;
			}
			if (RoomUtils.hasMoreThanTwoTotalCare(rooms)) {
				continue;
			}
			if (RoomUtils.hasMoreThanTwoIsolation(rooms)) {
				continue;
			}
			assignments.put(key, rooms);
			ArrayList<Room> filteredRoom = occupiedRooms.stream().filter(new Predicate<Room>() {

				@Override
				public boolean test(Room r) {
					// TODO Auto-generated method stub
					for (Room room : rooms) {
						if (r.getId() == room.getId()) {
							return false;
						}
					}
					return true;
				}
			}).collect(Collectors.toCollection(ArrayList::new));
			occupiedRooms.clear();
			occupiedRooms.addAll(filteredRoom);

		}
		if (occupiedRooms.isEmpty()) {
			// Assignments done! Display results.
			displayResults(assignments);
		}

	}

	private void displayResults(Map<String, List<Room>> assignments) {
		int group = 1;
		StringBuffer assignmentSb = new StringBuffer();
		for (String key : assignments.keySet()) {
			assignmentSb.append("Group ");
			assignmentSb.append(group);
			assignmentSb.append(": {");
			for (Room r : assignments.get(key)) {
				assignmentSb.append(r.getId());
				assignmentSb.append(" ");
			}
			assignmentSb.append("}\n");
			group++;
		}

		JOptionPane.showMessageDialog(appFrame, assignmentSb.toString(), "Assginments",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void saveViewData() {
		roomManager.saveAll(statusViewController.getRooms());
	}

	private void showApplicationView() {

		appFrame = new JFrame("Intel Assignment");
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		appFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				saveViewData();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				saveViewData();
			}
		});
		appFrame.getContentPane().add(applicationView, BorderLayout.CENTER);

		UiUtil.centerAndShow(appFrame);
	}

	private void addMessageForEmptyPatientLastName(StringBuffer sb, Room r) {
		if (StringUtils.hasText(r.getNurseFirstName()) == false) {
			sb.append("Room ");
			sb.append(r.getId());
			sb.append(" requires patient's last name.\n");
		}
	}

	private void addMessageForEmptyNurseFirstName(StringBuffer sb, Room r) {
		if (StringUtils.hasText(r.getNurseFirstName()) == false) {
			sb.append("Room ");
			sb.append(r.getId());
			sb.append(" requires a nurse's first name.\n");
		}
	}

	private void addMessageForEmptyPatientType(StringBuffer sb, Room r) {
		if (r.getPatientType().equals(PatientType.NONE)) {
			sb.append("Room ");
			sb.append(r.getId());
			sb.append(" requires patient type.\n");
		}
	}

	private boolean dataCompleted() {
		StringBuffer sb = new StringBuffer();
		for (Room r : statusViewController.getRooms()) {
			if (StringUtils.hasText(r.getPatientLastName())) {
				addMessageForEmptyPatientType(sb, r);
				addMessageForEmptyNurseFirstName(sb, r);
			}
			if (StringUtils.hasText(r.getNurseFirstName())) {
				addMessageForEmptyPatientType(sb, r);
				addMessageForEmptyPatientLastName(sb, r);
			}
			// A patient type was selected.
			if (!r.getPatientType().equals(PatientType.NONE)) {
				addMessageForEmptyPatientLastName(sb, r);
				addMessageForEmptyNurseFirstName(sb, r);
			}
		}

		String message = sb.toString();
		if (StringUtils.hasText(message)) {
			JOptionPane.showMessageDialog(applicationView, message, "Missing Information", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
}
