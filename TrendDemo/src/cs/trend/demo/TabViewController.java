package cs.trend.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cs.util.ui.UiUtil;

public class TabViewController {

	private TabView tabView;
	private ConfigurationPanel configurationPanel;
	private TrendPanel trendPanel;

	public JComponent getView() {
		return tabView;
	}

	public void init() {
		tabView = new TabView();
		tabView.init();

		configurationPanel = tabView.getConfigurationPanel();
		trendPanel = tabView.getTrendPanel();

		configureShowDifferenceCheckbox();
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
		configurationPanel.getRemovePlotButton().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						trendPanel.removePLot();
						trendPanel.showPlotsSeparately();
						resetShowDifferenceCheckbox();
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

	private void confgureAddPlotButton() {
		configurationPanel.getAddPlotButton().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						trendPanel
								.addPlot((CassetteDataPoint) configurationPanel
										.getDataPointCombo().getSelectedItem());
						onRealTimeCheckBoxPerformed();
						trendPanel.showPlotsSeparately();
						resetShowDifferenceCheckbox();
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
