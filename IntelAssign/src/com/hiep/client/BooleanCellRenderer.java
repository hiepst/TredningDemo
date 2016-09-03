package com.hiep.client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BooleanCellRenderer implements TableCellRenderer {

	private JCheckBox checkBox;

	private Color foreGround;

	private Color backGround;

	public BooleanCellRenderer() {
		checkBox = new JCheckBox();
		foreGround = checkBox.getForeground();
		backGround = checkBox.getBackground();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		checkBox.setSelected((boolean) value);
		if (isSelected) {
			checkBox.setForeground(table.getSelectionForeground());
			checkBox.setBackground(table.getSelectionBackground());
		} else {
			checkBox.setForeground(foreGround);
			checkBox.setBackground(backGround);
		}

		return checkBox;
	}

}
