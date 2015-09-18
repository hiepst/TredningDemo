package cs.trend.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import cs.trend.common.DataSourceUtil;
import cs.trend.common.DbUtil;
import cs.trend.dao.CsvDataSetDao;
import cs.trend.dao.DatasetDao;
import cs.util.ui.UiUtil;

public class TabViewController {

	private TabView tabView;

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
		confgureAddPlotButton();
		configureRemovePlotButton();
		configureShowLimitCheckbox();
		configureRealTimeCheckBox();
	}

	private void configureRealTimeCheckBox() {
		configurationPanel.getRealTimeCheckBox().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						onRealTimeCheckBoxPerformed();
					}
				});
	}

	private void configureShowLimitCheckbox() {
		configurationPanel.getShowLimitCheckbox().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						((TrendPanel) trendPanel)
								.showLimitSeries(configurationPanel
										.getShowLimitCheckbox().isSelected());
					}
				});
	}

	private void configureRemovePlotButton() {
		JTree tree = seriesSelectionView.getTree();

		JButton removePlotButton = configurationPanel.getRemovePlotButton();
		removePlotButton.setEnabled(false);
		removePlotButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				trendPanel.removePlot();
				trendPanel.showPlotsSeparately();
				// trendPanel.removeLimitMarkers(4);
				resetShowDifferenceCheckbox();
				resetShowAverageCheckbox();

				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
						.getSelectionModel().getSelectionPath()
						.getLastPathComponent();
				String displayName = (String) selectedNode.getUserObject();
			}
		});

		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();
				boolean enabled = newLeadSelectionPath != null
						&& newLeadSelectionPath.getLastPathComponent() != seriesSelectionView
								.getRootNode();

				removePlotButton.setEnabled(enabled);
			}
		});
	}

	private void resetShowDifferenceCheckbox() {
		// TODO Auto-generated method stub
		JCheckBox showDifferenceCheckbox = configurationPanel
				.getShowDifferenceCheckbox();
		showDifferenceCheckbox.setEnabled(trendPanel.getPlotCount() == 2);
		showDifferenceCheckbox.setSelected(false);
	}

	private void resetShowAverageCheckbox() {
		// TODO Auto-generated method stub
		JCheckBox showAverageCheckbox = configurationPanel
				.getShowAverageCheckbox();
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
		trendPanel.addLimitMarker("Warning Limit", Color.yellow,
				CsvDataSetDao.NOMINAL_HOT_1_TEMP + 2);
		trendPanel.addLimitMarker("Severe Limit", Color.red,
				CsvDataSetDao.NOMINAL_HOT_1_TEMP + 4);
		trendPanel.addLimitMarker("Warning Limit", Color.yellow,
				CsvDataSetDao.NOMINAL_HOT_1_TEMP - 2);
		trendPanel.addLimitMarker("Severe Limit", Color.red,
				CsvDataSetDao.NOMINAL_HOT_1_TEMP - 4);

		hot1LimitsAdded = true;
	}

	private void addHot2LimitMarkers() {
		if (hot2LimitsAdded) {
			return;
		}
		trendPanel.addLimitMarker("Hot 2 Warning Limit", Color.yellow,
				CsvDataSetDao.NOMINAL_HOT_2_TEMP + 2);
		trendPanel.addLimitMarker("Hot 2 Severe Limit", Color.red,
				CsvDataSetDao.NOMINAL_HOT_2_TEMP + 4);
		trendPanel.addLimitMarker("Hot 2 Warning Limit", Color.yellow,
				CsvDataSetDao.NOMINAL_HOT_2_TEMP - 2);
		trendPanel.addLimitMarker("Hot 2 Severe Limit", Color.red,
				CsvDataSetDao.NOMINAL_HOT_2_TEMP - 4);

		hot2LimitsAdded = true;
	}

	private void addCold1LimitMarkers() {
		if (cold1LimitsAdded) {
			return;
		}
		trendPanel.addLimitMarker("Cold 1 Warning Limit", Color.yellow,
				CsvDataSetDao.NOMINAL_COLD_1_TEMP + 2);
		trendPanel.addLimitMarker("Cold 1 Severe Limit", Color.red,
				CsvDataSetDao.NOMINAL_COLD_1_TEMP + 4);
		trendPanel.addLimitMarker("Cold 1 Warning Limit", Color.yellow,
				CsvDataSetDao.NOMINAL_COLD_1_TEMP - 2);
		trendPanel.addLimitMarker("Cold 1 Severe Limit", Color.red,
				CsvDataSetDao.NOMINAL_COLD_1_TEMP - 4);

		cold1LimitsAdded = true;
	}

	private void addCold2LimitMarkers() {
		if (cold2LimitsAdded) {
			return;
		}
		trendPanel.addLimitMarker("Cold 2 Warning Limit", Color.yellow,
				CsvDataSetDao.NOMINAL_COLD_2_TEMP + 2);
		trendPanel.addLimitMarker("Cold 2 Severe Limit", Color.red,
				CsvDataSetDao.NOMINAL_COLD_2_TEMP + 4);
		trendPanel.addLimitMarker("Cold 2 Warning Limit", Color.yellow,
				CsvDataSetDao.NOMINAL_COLD_2_TEMP - 2);
		trendPanel.addLimitMarker("Cold 2 Severe Limit", Color.red,
				CsvDataSetDao.NOMINAL_COLD_2_TEMP - 4);

		cold2LimitsAdded = true;
	}

	private void confgureAddPlotButton() {
		JButton addPlotButton = configurationPanel.getAddPlotButton();
		addPlotButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Source selectedSource = (Source) configurationPanel
						.getSourceCombo().getSelectedItem();
				CassetteDataPoint selectedDataPoint = (CassetteDataPoint) configurationPanel
						.getDataPointCombo().getSelectedItem();
				TimeSeries series = trendPanel.addPlot(selectedSource,
						selectedDataPoint);

				onRealTimeCheckBoxPerformed();
				trendPanel.showPlotsSeparately();
				resetShowDifferenceCheckbox();
				resetShowAverageCheckbox();

				seriesSelectionView.addObject(DataSourceUtil.getDisplayName(
						selectedSource, selectedDataPoint));

				switch (selectedDataPoint) {
				case HOT1_TEMPERATURE:
					addHot1LimitMarkers();
					break;
				case HOT2_TEMPERATURE:
					// addHot2LimitMarkers();
					addHot1LimitMarkers();
					break;
				case COLD1_TEMPERATURE:
					// addCold1LimitMarkers();
					break;
				case COLD2_TEMPERATURE:
					// addCold2LimitMarkers();
					break;
				case COLD_SET_POINT:
					// Ignore
					break;
				case HOT1_SET_POINT:
					// Ignore
					break;
				default:
					// Ignore
					break;
				}
			}

		});

	}

	private void configureDurartionSpinner() {
		configurationPanel.getDurartionSpinner().addChangeListener(
				new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						// TODO Auto-generated method stub
						DurationValue value = (DurationValue) configurationPanel
								.getDurartionSpinner().getValue();
						trendPanel.setDomainRange(value.getValue());
					}
				});
	}

	private void configureShowDifferenceCheckbox() {
		configurationPanel.getShowDifferenceCheckbox().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if (configurationPanel.getShowDifferenceCheckbox()
								.isSelected()) {
							trendPanel.showPlotDifference();
						} else {
							trendPanel.showPlotsSeparately();
						}
					}
				});
	}

	private void configureShowAverageCheckbox() {
		configurationPanel.getShowAverageCheckbox().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if (configurationPanel.getShowAverageCheckbox()
								.isSelected()) {
							int duration = ((DurationValue) configurationPanel
									.getDurartionSpinner().getValue())
									.getValue();
							trendPanel.showAveragePlot(duration, 0);
						} else {
							trendPanel.hideAveragePlot();
						}
					}
				});
	}

	private void onRealTimeCheckBoxPerformed() {
		boolean realtimeSelected = configurationPanel.getRealTimeCheckBox()
				.isSelected();

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

		TabViewController controller = new TabViewController();
		controller.setDao(new CsvDataSetDao("res/ScienceData_FLIGHT.csv"));
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
