package com.cs.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cs.client.util.ui.UiUtil;

/**
 * A demonstration application showing a time series chart where you can
 * dynamically add (random) data by clicking on a button.
 *
 */
public class ApplicationEntry {

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
		JFrame frame = new JFrame("Main Window - Trend Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		TrendAppController controller = new TrendAppController();
		controller.init();

		frame.getContentPane().add(controller.getView(), BorderLayout.CENTER);

		// Display the window.

		UiUtil.centerAndShow(frame);
	}

	public static void main(String[] args) {
		// TODO need to find out how to load the context from a different
		// directories
		// ApplicationContext appContext = new
		// ClassPathXmlApplicationContext("/resources/");
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				createAndShowGUI();
			}
		});
	}

}