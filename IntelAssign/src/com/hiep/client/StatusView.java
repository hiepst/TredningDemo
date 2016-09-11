package com.hiep.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableModel;

import com.hiep.common.Room;

public class StatusView extends JPanel {

	private static final long serialVersionUID = 2016082701L;

	private JSpinner totalRnSpinner;

	private JTable table;

	private TableModel tableModel;

	private List<Room> rooms;

	public void init() {
		tableModel = new StatusTableModel(rooms);
		table = new StatusTable(tableModel);
		table.setGridColor(Color.gray);

		SpinnerModel totalRnSpinnerModel = new SpinnerNumberModel(8, 1, 12, 1);
		totalRnSpinner = new JSpinner(totalRnSpinnerModel);

		JPanel northPanel = new JPanel();
		northPanel.add(new JLabel("Total RNs"));
		northPanel.add(totalRnSpinner);

		setLayout(new BorderLayout());
		add(northPanel, BorderLayout.NORTH);
		JScrollPane scrollPanel = new JScrollPane(table);
		add(scrollPanel, BorderLayout.CENTER);
	}

	public JSpinner getTotalRnSpinner() {
		return totalRnSpinner;
	}

	public JTable getTable() {
		return table;
	}

	public TableModel getTableModel() {
		return tableModel;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

}
