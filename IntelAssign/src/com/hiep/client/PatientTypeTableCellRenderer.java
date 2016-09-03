package com.hiep.client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.hiep.common.PatientType;

public class PatientTypeTableCellRenderer implements TableCellRenderer {

	private JComboBox<PatientType> comboBox;

	private Color foreGround;

	private Color backGround;

	public PatientTypeTableCellRenderer() {
		comboBox = new JComboBox<>(PatientType.values());
		foreGround = comboBox.getForeground();
		backGround = comboBox.getBackground();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		comboBox.setSelectedItem(value);
		if (isSelected) {
			comboBox.setForeground(table.getSelectionForeground());
			comboBox.setBackground(table.getSelectionBackground());
		} else {
			comboBox.setForeground(foreGround);
			comboBox.setBackground(backGround);
		}

		return comboBox;
	}

}
