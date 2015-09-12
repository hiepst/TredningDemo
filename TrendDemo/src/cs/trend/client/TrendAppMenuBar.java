package cs.trend.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import cs.util.ui.UiUtil;

public class TrendAppMenuBar extends JMenuBar {
	private static final long serialVersionUID = 2015090701;

	private JMenu fileMenu;

	private JMenu editMenu;

	private JMenu helpMenu;

	private JMenuItem openMenuItem;

	private JMenuItem saveMenuItem;

	private JMenuItem saveAsMenuItem;

	private JMenuItem newTabMenuItem;

	private JMenuItem newWindowMenuItem;

	private JMenuItem exportToCsvMenuItem;

	public JMenuItem getSaveAsMenuItem() {
		return saveAsMenuItem;
	}

	public JMenuItem getExportToCsvMenuItem() {
		return exportToCsvMenuItem;
	}

	private JMenuItem configMenuItem;

	private JMenuItem aboutMenuItem;

	public void init() {
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		add(fileMenu);

		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		add(editMenu);

		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		add(helpMenu);

		// a group of JMenuItems
		newTabMenuItem = new JMenuItem("New Tab", KeyEvent.VK_T);
		newTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
				ActionEvent.CTRL_MASK));

		newWindowMenuItem = new JMenuItem("New Window", KeyEvent.VK_N);
		newWindowMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));

		openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));

		saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		saveAsMenuItem = new JMenuItem("Save As");
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));

		exportToCsvMenuItem = new JMenuItem("Export to CSV", KeyEvent.VK_X);
		exportToCsvMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_X, ActionEvent.CTRL_MASK));

		fileMenu.add(newTabMenuItem);
		fileMenu.add(newWindowMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(exportToCsvMenuItem);

		configMenuItem = new JMenuItem("Configuration");
		editMenu.add(configMenuItem);

		aboutMenuItem = new JMenuItem("About");
		helpMenu.add(aboutMenuItem);
	}

	public JMenuItem getSaveMenuItem() {
		return saveMenuItem;
	}

	public JMenuItem getOpenMenuItem() {
		return openMenuItem;
	}

	public JMenu getFileMenu() {
		return fileMenu;
	}

	public JMenu getEditMenu() {
		return editMenu;
	}

	public JMenu getHelpMenu() {
		return helpMenu;
	}

	public JMenuItem getConfigMenuItem() {
		return configMenuItem;
	}

	public JMenuItem getAboutMenuItem() {
		return aboutMenuItem;
	}

	public JMenuItem getNewTabMenuItem() {
		return newTabMenuItem;
	}

	public JMenuItem getNewWindowMenuItem() {
		return newWindowMenuItem;
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
		JFrame frame = new JFrame("Trend App Menu Bar");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		TrendAppMenuBar menuBar = new TrendAppMenuBar();
		menuBar.init();

		frame.getContentPane().setPreferredSize(new Dimension(400, 400));
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);

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
