package com.cs.client;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.cs.client.util.ui.UiUtil;

public class TrendAppView extends JPanel {

	private static final long serialVersionUID = 2015090701;

	private TrendAppMenuBar menuBar;

	private JTabbedPane tabbedPane;

	public void init() {
		menuBar = new TrendAppMenuBar();
		menuBar.init();

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		this.setLayout(new BorderLayout());
		this.add(menuBar, BorderLayout.NORTH);
		this.add(tabbedPane, BorderLayout.CENTER);
	}

	public TrendAppMenuBar getMenuBar() {
		return menuBar;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		// Create and set up the window.
		JFrame frame = new JFrame("Trend App View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		TrendAppView trendAppView = new TrendAppView();
		trendAppView.init();

		frame.getContentPane().setPreferredSize(new Dimension(400, 400));
		frame.getContentPane().add(trendAppView, BorderLayout.CENTER);

		// Display the window.
		UiUtil.centerAndShow(frame);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				createAndShowGUI();
			}
		});
	}

}
