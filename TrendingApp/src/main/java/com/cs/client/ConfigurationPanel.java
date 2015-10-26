package com.cs.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.cs.client.enumType.Cassette;
import com.cs.client.enumType.CassetteDataPoint;
import com.cs.client.enumType.DurationValue;
import com.cs.client.enumType.Locker;
import com.cs.client.util.ui.UiUtil;

public class ConfigurationPanel extends JPanel {

	private static final long serialVersionUID = 2015090701;

	private JComboBox<Locker> lockerCombo;

	private JComboBox<Cassette> cassetteCombo;

	private JComboBox<CassetteDataPoint> dataPointCombo;

	private JButton startButton;

	private JButton addSeriesButton;

	private JButton removeSeriesButton;

	private JButton stopButton;

	private JTextField startDateField;

	private JTextField startTimeField;

	private JTextField endTimeField;

	private JTextField endDateField;

	private JSpinner durartionSpinner;

	private JCheckBox realTimeCheckBox;

	private JCheckBox showLimitCheckbox;

	private JCheckBox showDifferenceCheckbox;

	private JCheckBox showAverageCheckbox;

	public void init() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.ipady = 5;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("Locker:"), c);

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		lockerCombo = new JComboBox<Locker>(Locker.values());
		add(lockerCombo, c);

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		add(new JLabel("Cassette:"), c);

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		cassetteCombo = new JComboBox<Cassette>(Cassette.values());
		add(cassetteCombo, c);

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		add(new JLabel("Data Point:"), c);

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		dataPointCombo = new JComboBox<CassetteDataPoint>(CassetteDataPoint.values());
		add(dataPointCombo, c);

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		add(new JLabel("Start Time:"), c);

		c.gridx = 0;
		c.gridy++;

		Instant now = Instant.now();
		startDateField = new JTextField("09/02/15");
		add(startDateField, c);

		c.gridx = 1;
		startTimeField = new JTextField("2:15 PM");
		add(startTimeField, c);

		c.gridx = 0;
		c.gridy++;
		add(new JLabel("End Time:"), c);

		c.gridx = 0;
		c.gridy++;
		endDateField = new JTextField("09/02/15");

		add(endDateField, c);

		c.gridx = 1;
		endTimeField = new JTextField("2:20 PM");
		add(endTimeField, c);

		setDateTimeFieldEnabled(false);

		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Display Window:"), c);

		c.gridx = 0;
		c.gridy++;

		durartionSpinner = new JSpinner(new SpinnerListModel(DurationValue.values()));
		add(durartionSpinner, c);

		c.gridx = 1;
		realTimeCheckBox = new JCheckBox("Real Time");
		realTimeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		realTimeCheckBox.setSelected(true);
		realTimeCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setDateTimeFieldEnabled(!realTimeCheckBox.isSelected());
			}
		});
		add(realTimeCheckBox, c);

		c.gridx = 0;
		c.gridy++;
		showLimitCheckbox = new JCheckBox("Show Limits");
		showLimitCheckbox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		showLimitCheckbox.setSelected(true);
		add(showLimitCheckbox, c);

		c.gridx = 1;
		showDifferenceCheckbox = new JCheckBox("Show Difference");
		showDifferenceCheckbox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		showDifferenceCheckbox.setSelected(false);
		showDifferenceCheckbox.setEnabled(false);
		add(showDifferenceCheckbox, c);

		c.gridx = 0;
		c.gridy++;
		showAverageCheckbox = new JCheckBox("Show Average");
		showAverageCheckbox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		showAverageCheckbox.setSelected(false);
		showAverageCheckbox.setEnabled(false);
		add(showAverageCheckbox, c);

		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		add(Box.createRigidArea(new Dimension(1, 15)), c);

		c.gridx = 0;
		c.gridy++;
		addSeriesButton = new JButton("Add Series");
		add(addSeriesButton, c);

		c.gridx = 1;
		removeSeriesButton = new JButton("Remove Series");
		add(removeSeriesButton, c);
	}

	public JCheckBox getShowAverageCheckbox() {
		return showAverageCheckbox;
	}

	public JComboBox<Locker> getLockerCombo() {
		return lockerCombo;
	}

	public JComboBox<Cassette> getCassetteCombo() {
		return cassetteCombo;
	}

	public JButton getAddSeriesButton() {
		return addSeriesButton;
	}

	public JButton getRemoveSeriesButton() {
		return removeSeriesButton;
	}

	public JButton getStartButton() {
		return startButton;
	}

	public JButton getStopButton() {
		return stopButton;
	}

	public JCheckBox getShowDifferenceCheckbox() {
		return showDifferenceCheckbox;
	}

	public JComboBox<CassetteDataPoint> getDataPointCombo() {
		return dataPointCombo;
	}

	public JTextField getStartDateField() {
		return startDateField;
	}

	public JTextField getStartTimeField() {
		return startTimeField;
	}

	public JTextField getEndTimeField() {
		return endTimeField;
	}

	public JTextField getEndDateField() {
		return endDateField;
	}

	public JCheckBox getShowLimitCheckbox() {
		return showLimitCheckbox;
	}

	public JSpinner getDurartionSpinner() {
		return durartionSpinner;
	}

	public void setDateTimeFieldEnabled(boolean enabled) {
		startDateField.setEnabled(enabled);
		startTimeField.setEnabled(enabled);
		endDateField.setEnabled(enabled);
		endTimeField.setEnabled(enabled);
	}

	public JCheckBox getRealTimeCheckBox() {
		return realTimeCheckBox;
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
		JFrame frame = new JFrame("Configuration Panel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ConfigurationPanel configPanel = new ConfigurationPanel();
		configPanel.init();

		frame.getContentPane().add(configPanel, BorderLayout.CENTER);

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
