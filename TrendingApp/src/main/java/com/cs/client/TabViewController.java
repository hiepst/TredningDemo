package com.cs.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jfree.data.time.TimeSeries;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.client.RestTemplate;

import com.cs.client.dao.CsvDataSetDao;
import com.cs.client.dao.DatasetDao;
import com.cs.client.enumType.Cassette;
import com.cs.client.enumType.CassetteDataPoint;
import com.cs.client.enumType.DurationValue;
import com.cs.client.util.DataSourceUtil;
import com.cs.client.util.ui.UiUtil;
import com.cs.domain.DataPointSeries;

public class TabViewController implements InitializingBean {

	private TabView tabView;

	public void setTabView(TabView tabView) {
		this.tabView = tabView;
	}

	private ConfigurationPanel configurationPanel;

	private TrendPanel trendPanel;

	private boolean hot1LimitsAdded = false;

	private boolean hot2LimitsAdded = false;

	private boolean cold1LimitsAdded = false;

	private boolean cold2LimitsAdded = false;

	public JComponent getView() {
		return tabView;
	}

	private DatasetDao dao;

	private SeriesSelectionView seriesSelectionView;

	public void setDao(DatasetDao dao) {
		this.dao = dao;
	}

	public void init() {
		tabView = new TabView();
		tabView.setDao(dao);
		tabView.init();

		configurationPanel = tabView.getConfigurationPanel();
		trendPanel = tabView.getTrendPanel();
		seriesSelectionView = tabView.getSeriesSelectionView();

		configureShowDifferenceCheckbox();
		configureShowAverageCheckbox();
		configureDurartionSpinner();
		confgureAddSeriesButton();
		configureRemoveSeriesButton();
		configureShowLimitCheckbox();
		configureRealTimeCheckBox();
	}

	private void configureRealTimeCheckBox() {
		configurationPanel.getRealTimeCheckBox().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				onRealTimeCheckBoxPerformed();
			}
		});
	}

	private void configureShowLimitCheckbox() {
		configurationPanel.getShowLimitCheckbox().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				((TrendPanel) trendPanel).showLimitSeries(configurationPanel.getShowLimitCheckbox().isSelected());
			}
		});
	}

	private void configureRemoveSeriesButton() {
		JTree tree = seriesSelectionView.getTree();

		JButton removeSeriesButton = configurationPanel.getRemoveSeriesButton();
		removeSeriesButton.setEnabled(false);
		removeSeriesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionModel()
						.getSelectionPath().getLastPathComponent();
				String displayName = (String) selectedNode.getUserObject();
				trendPanel.removePlot(displayName);
				trendPanel.showPlotsSeparately();
				// trendPanel.removeLimitMarkers(4);
				resetShowDifferenceCheckbox();
				resetShowAverageCheckbox();
				seriesSelectionView.removeCurrentNode();
				onRealTimeCheckBoxPerformed();
			}
		});

		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();
				boolean enabled = newLeadSelectionPath != null
						&& newLeadSelectionPath.getLastPathComponent() != seriesSelectionView.getRootNode();

				removeSeriesButton.setEnabled(enabled);
			}
		});
	}

	private void resetShowDifferenceCheckbox() {
		// TODO Auto-generated method stub
		JCheckBox showDifferenceCheckbox = configurationPanel.getShowDifferenceCheckbox();
		showDifferenceCheckbox.setEnabled(trendPanel.getPlotCount() == 2);
		showDifferenceCheckbox.setSelected(false);
	}

	private void resetShowAverageCheckbox() {
		// TODO Auto-generated method stub
		JCheckBox showAverageCheckbox = configurationPanel.getShowAverageCheckbox();
		showAverageCheckbox.setEnabled(trendPanel.getPlotCount() == 1);
		showAverageCheckbox.setSelected(false);
	}

	// private void addHot1LimitMarkers() {
	// if (hot1LimitsAdded) {
	// return;
	// }
	// trendPanel.addLimitMarker("Hot 1 Warning Limit", Color.yellow,
	// CsvDataSetDao.NOMINAL_HOT_1_TEMP + 2);
	// trendPanel.addLimitMarker("Hot 1 Severe Limit", Color.red,
	// CsvDataSetDao.NOMINAL_HOT_1_TEMP + 4);
	// trendPanel.addLimitMarker("Hot 1 Warning Limit", Color.yellow,
	// CsvDataSetDao.NOMINAL_HOT_1_TEMP - 2);
	// trendPanel.addLimitMarker("Hot 1 Severe Limit", Color.red,
	// CsvDataSetDao.NOMINAL_HOT_1_TEMP - 4);
	//
	// hot1LimitsAdded = true;
	// }
	private void addHot1LimitMarkers() {
		if (hot1LimitsAdded) {
			return;
		}
		trendPanel.addLimitMarker("Warning Limit", Color.yellow, CsvDataSetDao.NOMINAL_HOT_1_TEMP + 2);
		trendPanel.addLimitMarker("Severe Limit", Color.red, CsvDataSetDao.NOMINAL_HOT_1_TEMP + 4);
		trendPanel.addLimitMarker("Warning Limit", Color.yellow, CsvDataSetDao.NOMINAL_HOT_1_TEMP - 2);
		trendPanel.addLimitMarker("Severe Limit", Color.red, CsvDataSetDao.NOMINAL_HOT_1_TEMP - 4);

		hot1LimitsAdded = true;
	}

	private void addHot2LimitMarkers() {
		if (hot2LimitsAdded) {
			return;
		}
		trendPanel.addLimitMarker("Hot 2 Warning Limit", Color.yellow, CsvDataSetDao.NOMINAL_HOT_2_TEMP + 2);
		trendPanel.addLimitMarker("Hot 2 Severe Limit", Color.red, CsvDataSetDao.NOMINAL_HOT_2_TEMP + 4);
		trendPanel.addLimitMarker("Hot 2 Warning Limit", Color.yellow, CsvDataSetDao.NOMINAL_HOT_2_TEMP - 2);
		trendPanel.addLimitMarker("Hot 2 Severe Limit", Color.red, CsvDataSetDao.NOMINAL_HOT_2_TEMP - 4);

		hot2LimitsAdded = true;
	}

	private void addCold1LimitMarkers() {
		if (cold1LimitsAdded) {
			return;
		}
		trendPanel.addLimitMarker("Cold 1 Warning Limit", Color.yellow, CsvDataSetDao.NOMINAL_COLD_1_TEMP + 2);
		trendPanel.addLimitMarker("Cold 1 Severe Limit", Color.red, CsvDataSetDao.NOMINAL_COLD_1_TEMP + 4);
		trendPanel.addLimitMarker("Cold 1 Warning Limit", Color.yellow, CsvDataSetDao.NOMINAL_COLD_1_TEMP - 2);
		trendPanel.addLimitMarker("Cold 1 Severe Limit", Color.red, CsvDataSetDao.NOMINAL_COLD_1_TEMP - 4);

		cold1LimitsAdded = true;
	}

	private void addCold2LimitMarkers() {
		if (cold2LimitsAdded) {
			return;
		}
		trendPanel.addLimitMarker("Cold 2 Warning Limit", Color.yellow, CsvDataSetDao.NOMINAL_COLD_2_TEMP + 2);
		trendPanel.addLimitMarker("Cold 2 Severe Limit", Color.red, CsvDataSetDao.NOMINAL_COLD_2_TEMP + 4);
		trendPanel.addLimitMarker("Cold 2 Warning Limit", Color.yellow, CsvDataSetDao.NOMINAL_COLD_2_TEMP - 2);
		trendPanel.addLimitMarker("Cold 2 Severe Limit", Color.red, CsvDataSetDao.NOMINAL_COLD_2_TEMP - 4);

		cold2LimitsAdded = true;
	}

	/**
	 * Test hardness. TODO Remove after testing
	 */
	private void testRestBackend() {
		final String uri = "http://localhost:8080/TrendingApp/{dataPoint}/{startTime}/{endTime}";

		Map<String, String> params = new HashMap<String, String>();
		params.put("dataPoint", "data point name");
		params.put("startTime", "2015-01-01T02:00:00");
		params.put("endTime", "2015-12-01T02:00:00");

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				RestTemplate restTemplate = new RestTemplate();
				DataPointSeries result = restTemplate.getForObject(uri, DataPointSeries.class, params);

				System.out.println(result.toString());
			}
		}, 1000, 1000);

	}

	private void confgureAddSeriesButton() {
		JButton addSeriesButton = configurationPanel.getAddSeriesButton();
		addSeriesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				testRestBackend();

				// TODO Auto-generated method stub
				Cassette selectedSource = (Cassette) configurationPanel.getCassetteCombo().getSelectedItem();
				CassetteDataPoint selectedDataPoint = (CassetteDataPoint) configurationPanel.getDataPointCombo()
						.getSelectedItem();
				TimeSeries series = trendPanel
						.addPlot(DataSourceUtil.getDisplayName(selectedSource, selectedDataPoint));

				onRealTimeCheckBoxPerformed();
				trendPanel.showPlotsSeparately();
				resetShowDifferenceCheckbox();
				resetShowAverageCheckbox();

				seriesSelectionView.addObject(DataSourceUtil.getDisplayName(selectedSource, selectedDataPoint));

				switch (selectedDataPoint) {
				case HOT1_TEMP:
					addHot1LimitMarkers();
					break;
				case HOT2_TEMP:
					// addHot2LimitMarkers();
					addHot1LimitMarkers();
					break;
				case COLD1_TEMP:
					// addCold1LimitMarkers();
					break;
				case HUMIDITY:
					break;
				case PUMP_CURRENT:
					break;
				default:
					break;
				}

			}

		});

	}

	private void configureDurartionSpinner() {
		configurationPanel.getDurartionSpinner().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				DurationValue value = (DurationValue) configurationPanel.getDurartionSpinner().getValue();
				trendPanel.setDomainRange(value.getValue());
			}
		});
	}

	private void configureShowDifferenceCheckbox() {
		configurationPanel.getShowDifferenceCheckbox().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (configurationPanel.getShowDifferenceCheckbox().isSelected()) {
					trendPanel.showPlotDifference();
				} else {
					trendPanel.showPlotsSeparately();
				}
			}
		});
	}

	private void configureShowAverageCheckbox() {
		configurationPanel.getShowAverageCheckbox().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (configurationPanel.getShowAverageCheckbox().isSelected()) {
					int duration = ((DurationValue) configurationPanel.getDurartionSpinner().getValue()).getValue();
					trendPanel.showAveragePlot(duration, 0);
				} else {
					trendPanel.hideAveragePlot();
				}
			}
		});
	}

	private void onRealTimeCheckBoxPerformed() {
		boolean realtimeSelected = configurationPanel.getRealTimeCheckBox().isSelected();

		configurationPanel.setDateTimeFieldEnabled(!realtimeSelected);

		if (realtimeSelected) {
			trendPanel.startTrending();
		} else {
			trendPanel.stopTrending();
		}
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
		JFrame frame = new JFrame("Tab View Controller");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		DatasetDao dao = new CsvDataSetDao();
		dao.setFileName("resources/ScienceData_FLIGHT.csv");
		dao.init();

		TabViewController controller = new TabViewController();
		controller.setDao(dao);
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

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		init();
	}
}
