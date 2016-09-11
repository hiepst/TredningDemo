package com.hiep.client;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.hiep.common.PatientType;

public class PatientTypeTableCellRenderer implements TableCellRenderer {

	private JComboBox<PatientType> comboBox;

	public PatientTypeTableCellRenderer() {
		comboBox = new JComboBox<>(PatientType.values());
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		comboBox.setSelectedItem(value);

		return comboBox;
	}

}
