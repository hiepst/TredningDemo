package com.cs.client;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.springframework.beans.factory.InitializingBean;

import com.cs.client.dao.CsvDataSetDao;
import com.cs.client.dao.DatasetDao;
import com.cs.client.util.ui.UiUtil;

public class TabView extends JPanel implements InitializingBean {

	private static final long serialVersionUID = 2015090701;

	private JPanel bottomRightPanel;

	private JSplitPane mainSplitPane;

	private JSplitPane rightSplitPane;

	private TrendPanel trendPanel;

	private ConfigurationPanel configurationPanel;

	private SeriesSelectionView seriesSelectionView;

	private DatasetDao dao;

	public SeriesSelectionView getSeriesSelectionView() {
		return seriesSelectionView;
	}

	public TrendPanel getTrendPanel() {
		return trendPanel;
	}

	public ConfigurationPanel getConfigurationPanel() {
		return configurationPanel;
	}

	public void setDao(DatasetDao dao) {
		this.dao = dao;
	}

	public void init() {
		trendPanel = new TrendPanel();
		trendPanel.setDao(dao);
		trendPanel.init();

		configurationPanel = new ConfigurationPanel();
		configurationPanel.init();

		seriesSelectionView = new SeriesSelectionView();
		seriesSelectionView.init();

		JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
		leftPanel.add(configurationPanel, BorderLayout.NORTH);
		leftPanel.add(seriesSelectionView, BorderLayout.CENTER);
		leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		bottomRightPanel = new JPanel(new BorderLayout());

		rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, trendPanel, bottomRightPanel);
		rightSplitPane.setOneTouchExpandable(true);
		rightSplitPane.setResizeWeight(1.0);

		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPanel, rightSplitPane);
		mainSplitPane.setOneTouchExpandable(true);

		this.setLayout(new BorderLayout());
		this.add(mainSplitPane, BorderLayout.CENTER);
	}

	public JPanel getBottomRightPanel() {
		return bottomRightPanel;
	}

	public JSplitPane getMainSplitPane() {
		return mainSplitPane;
	}

	public JSplitPane getRightSplitPane() {
		return rightSplitPane;
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
		JFrame frame = new JFrame("Tab View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		DatasetDao dao = new CsvDataSetDao();
		dao.setFileName("resources/ScienceData_FLIGHT.csv");
		dao.init();
		TabView tabView = new TabView();
		tabView.setDao(dao);
		tabView.init();

		frame.getContentPane().add(tabView, BorderLayout.CENTER);

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

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		init();
	}
}
