package com.hiep.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.hiep.common.PatientType;
import com.hiep.common.Room;

public class StatusView extends JPanel {

	private static final long serialVersionUID = 2016082701L;

	private JSpinner totalRnSpinner;

	private JTable table;

	private TableModel tableModel;

	private StatusViewController controller;

	public void init() {
		tableModel = new StatusTableModel(controller.getRooms());
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

	public void setController(StatusViewController controller) {
		this.controller = controller;
	}

	private class StatusTable extends JTable {

		private static final long serialVersionUID = 2016082701L;

		public StatusTable(TableModel tableModel) {
			super(tableModel);
			rowHeight = 25;
		}

		@Override
		public TableCellRenderer getCellRenderer(int row, int column) {
			StatusTableColumn tableColumn = StatusTableColumn.values()[column];
			switch (tableColumn) {
			case PATIENT_TYPE:
				return new PatientTypeTableCellRenderer();
			case PATIENT_LASTNAME:
				return super.getCellRenderer(row, column);
			case NURSE:
				return super.getCellRenderer(row, column);
			case ROOM:
				return super.getCellRenderer(row, column);
			default:
				return new BooleanCellRenderer();
			}
		}

		@Override
		public TableCellEditor getCellEditor(int row, int column) {

			StatusTableColumn tableColumn = StatusTableColumn.values()[column];
			switch (tableColumn) {
			case PATIENT_TYPE:
				JComboBox<PatientType> comboBox = new JComboBox<>(PatientType.values());
				return new DefaultCellEditor(comboBox);
			// case PATIENT_LASTNAME:
			// return new DefaultCellEditor(new JCheckBox());
			// case POST_SURG:
			// return new DefaultCellEditor(new JCheckBox());
			// case CHEST_TUBE:
			// return new DefaultCellEditor(new JCheckBox());
			// case ISOLATION:
			// return new DefaultCellEditor(new JCheckBox());
			// case MED_DRIP:
			// return new DefaultCellEditor(new JCheckBox());
			// case POSSIBLE_DC:
			// return new DefaultCellEditor(new JCheckBox());
			// case WOUND_CARE:
			// return new DefaultCellEditor(new JCheckBox());
			// case NURSE:
			// return new DefaultCellEditor(new JCheckBox());
			default:
				return super.getCellEditor(row, column);

			}
		}
	}

	private class StatusTableModel extends AbstractTableModel {
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			Room room = entries.get(rowIndex);
			StatusTableColumn tableColumn = StatusTableColumn.values()[columnIndex];

			switch (tableColumn) {
			case ROOM:
				break;
			case PATIENT_TYPE:
				room.setPatientType((PatientType) value);
				break;
			case PATIENT_LASTNAME:
				room.setPatientLastName((String) value);
				break;
			case POST_SURG:
				room.setPostSurg((boolean) value);
				break;
			case CHEST_TUBE:
				room.setChestTube((boolean) value);
				break;
			case ISOLATION:
				room.setIsolation((boolean) value);
				break;
			case MED_DRIP:
				room.setMedDrip((boolean) value);
				break;
			case POSSIBLE_DC:
				room.setPossibleDc((boolean) value);
				break;
			case WOUND_CARE:
				room.setWoundCare((boolean) value);
				break;
			case NURSE:
				room.setNurse((String) value);
				break;
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			boolean editable = !StatusTableColumn.values()[columnIndex].equals(StatusTableColumn.ROOM);
			return editable;
		}

		@Override
		public String getColumnName(int column) {
			// TODO Auto-generated method stub
			return StatusTableColumn.values()[column].getDisplayName();
		}

		private static final long serialVersionUID = 2016082701L;

		private List<Room> entries = new ArrayList<>();

		public StatusTableModel(List<Room> entries) {
			super();
			this.entries = entries;
		}

		@Override
		public int getRowCount() {
			return entries.size();
		}

		@Override
		public int getColumnCount() {
			return StatusTableColumn.values().length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Room room = entries.get(rowIndex);
			switch (StatusTableColumn.values()[columnIndex]) {
			case ROOM:
				return room.getId();
			case PATIENT_TYPE:
				return room.getPatientType();
			case POST_SURG:
				return room.isPostSurg();
			case MED_DRIP:
				return room.isMedDrip();
			case CHEST_TUBE:
				return room.isChestTube();
			case WOUND_CARE:
				return room.isWoundCare();
			case ISOLATION:
				return room.isIsolation();
			case POSSIBLE_DC:
				return room.isPossibleDc();
			case NURSE:
				return room.getNurse();
			case PATIENT_LASTNAME:
				return room.getPatientLastName();
			}

			return null;
		}
	}

}
