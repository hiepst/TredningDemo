package com.hiep.client;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class ApplicationView extends JPanel {

	private static final long serialVersionUID = 2016082701L;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu helpMenu;

	private JMenuItem roomItem;
	private JMenuItem aboutItem;

	private JPanel mainPanel;

	private JButton generateAssignmentButton;

	public void init() {

		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		helpMenu = new JMenu("Help");
		aboutItem = new JMenuItem("About");
		helpMenu.add(aboutItem);

		roomItem = new JMenuItem("Room...");
		editMenu.add(roomItem);

		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);

		generateAssignmentButton = new JButton("Generate Assignment");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(generateAssignmentButton);

		setLayout(new BorderLayout());

		add(menuBar, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public JComponent getComponent() {
		return this;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public JButton getGenerateAssignmentButton() {
		return generateAssignmentButton;
	}

}
