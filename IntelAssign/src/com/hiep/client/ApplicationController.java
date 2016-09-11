package com.hiep.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.hiep.common.PatientType;
import com.hiep.common.Room;
import com.hiep.common.util.StringUtils;
import com.hiep.common.util.UiUtil;
import com.hiep.server.RoomManager;

public class ApplicationController {
	private RoomManager roomManager;
	private StatusViewController statusViewController;
	private ApplicationView view;
	private JFrame appFrame;

	public void init() {
		roomManager = new RoomManager();

		statusViewController = new StatusViewController();
		statusViewController.setRoomManager(roomManager);
		statusViewController.init();

		view = new ApplicationView();
		view.setPreferredSize(new Dimension(1000, 800));
		view.setMainPanel(statusViewController.getView());
		view.init();

		view.getGenerateAssignmentButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// if (!dataCompleted()) {
				// return;
				// }
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(view, "Show the assignments by groups.");
				saveViewData();
			}

		});

		view.getExitMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveViewData();
				appFrame.dispose();
			}
		});

		view.getAboutMenuItem().addActionListener(new ActionListener() {

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
		appFrame.getContentPane().add(view, BorderLayout.CENTER);

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
			JOptionPane.showMessageDialog(view, message, "Missing Information", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
}
