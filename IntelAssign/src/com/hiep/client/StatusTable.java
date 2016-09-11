package com.hiep.client;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.hiep.common.PatientType;

public class StatusTable extends JTable {

	private static final long serialVersionUID = 2016082701L;

	public StatusTable(TableModel tableModel) {
		super(tableModel);
		rowHeight = 25;
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		StatusTableColumn tableColumn = getTableColumn(column);
		switch (tableColumn) {
		case PATIENT_TYPE:
			return new PatientTypeTableCellRenderer();
		case PATIENT_LASTNAME:
			return super.getCellRenderer(row, column);
		case NURSE_FIRSTNAME:
			return super.getCellRenderer(row, column);
		case ROOM:
			return super.getCellRenderer(row, column);
		default:
			return new BooleanCellRenderer();
		}
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {

		StatusTableColumn tableColumn = getTableColumn(column);
		switch (tableColumn) {
		case PATIENT_TYPE:
			JComboBox<PatientType> comboBox = new JComboBox<>(PatientType.values());
			return new DefaultCellEditor(comboBox);
		case PATIENT_LASTNAME:
			return super.getCellEditor(row, column);
		case NURSE_FIRSTNAME:
			return super.getCellEditor(row, column);
		case ROOM:
			return super.getCellEditor(row, column);
		default:
			return new DefaultCellEditor(new JCheckBox());
		}
	}

	private StatusTableColumn getTableColumn(int column) {
		return StatusTableColumn.values()[column];
	}

}
