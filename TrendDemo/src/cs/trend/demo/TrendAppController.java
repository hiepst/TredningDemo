package cs.trend.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;

import cs.framework.ButtonTabComponent;
import cs.trend.dao.CsvDataSetDao;
import cs.util.ui.UiUtil;

/**
 * Controller for the trend application
 * 
 * @author hvhong
 *
 */
public class TrendAppController {

	private static int windowCounter = 1;

	private int tabCounter = 0;

	private TrendAppView trendAppView;

	private JFileChooser fileChooser;

	private JTabbedPane tabbedPane;

	private TrendAppMenuBar menuBar;

	public JComponent getView() {
		return trendAppView;
	}

	public void init() {

		trendAppView = new TrendAppView();
		trendAppView.init();

		menuBar = trendAppView.getMenuBar();

		initOpenMenuItem();
		initSaveMenuItem();
		initSaveAsMenuItem();
		intNewTabMenuItem();
		initNewWindowMenuItem();
		initExportToCsvMenuItem();

		tabbedPane = trendAppView.getTabbedPane();
		addTab();

		fileChooser = new JFileChooser();
	}

	public void addTab() {
		TabViewController tabViewController = new TabViewController();
		tabViewController.setDao(new CsvDataSetDao("res/ScienceData_FLIGHT.csv"));
		tabViewController.init();

		tabbedPane.add("Tab " + (tabCounter + 1), tabViewController.getView());
		initTabComponent(tabCounter);

		tabCounter++;
	}

	private void initTabComponent(int i) {
		tabbedPane.setTabComponentAt(i, new ButtonTabComponent(tabbedPane));
	}

	private void initNewWindowMenuItem() {
		menuBar.getNewWindowMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Schedule a job for the event-dispatching thread:
				// creating and showing this application's GUI.
				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						createAndShowNewWindow();
					}
				});
			}
		});
	}

	private void intNewTabMenuItem() {
		menuBar.getNewTabMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				addTab();
			}
		});
	}

	private void initSaveMenuItem() {
		menuBar.getSaveMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileChooser.setDialogTitle("Saving a Trend");
				fileChooser.setFileFilter(new FileNameExtensionFilter("Trend Files", "trd"));
				fileChooser.showSaveDialog(trendAppView);
			}
		});
	}

	private void initSaveAsMenuItem() {
		menuBar.getSaveAsMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileChooser.setDialogTitle("Saving As Another Trend");
				fileChooser.setFileFilter(new FileNameExtensionFilter("Trend Files", "trd"));
				fileChooser.showSaveDialog(trendAppView);
			}
		});
	}

	private void initExportToCsvMenuItem() {
		menuBar.getExportToCsvMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileChooser.setDialogTitle("Exporting to CSV file");
				fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
				fileChooser.showSaveDialog(trendAppView);
			}
		});
	}

	private void initOpenMenuItem() {
		menuBar.getOpenMenuItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileChooser.setDialogTitle("Selecting a Trend to Open");
				fileChooser.setFileFilter(new FileNameExtensionFilter("Trend Files", "trd"));
				fileChooser.showOpenDialog(trendAppView);
			}
		});
	}

	private void createAndShowNewWindow() {
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
		windowCounter++;

		// Create and set up the window.
		JFrame frame = new JFrame("Window " + windowCounter + " - Trend Demo");
		TrendAppController controller = new TrendAppController();
		controller.init();

		frame.getContentPane().add(controller.getView(), BorderLayout.CENTER);

		// Display the window.
		UiUtil.centerAndShow(frame);

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
		JFrame frame = new JFrame("Trend App Controller");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		TrendAppController controller = new TrendAppController();
		controller.init();

		frame.getContentPane().add(controller.getView(), BorderLayout.CENTER);

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
