package com.hiep.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.hiep.common.util.UiUtil;

public class ApplicationController {
	public void init() {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				showApplicationView();
			}
		});

	}

	private static void showApplicationView() {
		StatusViewController statusViewController = new StatusViewController();
		statusViewController.init();

		ApplicationView view = new ApplicationView();
		view.setPreferredSize(new Dimension(800, 700));
		view.setMainPanel(statusViewController.getView());
		view.init();

		view.getGenerateAssignmentButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(view, "Show the assignments by groups.");
			}
		});

		JFrame frame = new JFrame("Intel Assignment");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(view, BorderLayout.CENTER);

		UiUtil.centerAndShow(frame);
	}
}
