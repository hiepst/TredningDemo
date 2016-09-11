package com.hiep.client;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.hiep.common.PatientType;
import com.hiep.common.Room;

public class StatusTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2016082701L;

	private List<Room> entries = new ArrayList<>();

	public StatusTableModel(List<Room> entries) {
		super();
		this.entries = entries;
	}

	private StatusTableColumn getTableColumn(int columnIndex) {
		return StatusTableColumn.values()[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		boolean editable = !getTableColumn(columnIndex).equals(StatusTableColumn.ROOM);
		return editable;
	}

	@Override
	public String getColumnName(int column) {
		return getTableColumn(column).getDisplayName();
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
		switch (getTableColumn(columnIndex)) {
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
		case NURSE_FIRSTNAME:
			return room.getNurseFirstName();
		case PATIENT_LASTNAME:
			return room.getPatientLastName();
		}

		return null;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Room room = entries.get(rowIndex);
		StatusTableColumn tableColumn = getTableColumn(columnIndex);

		switch (tableColumn) {
		case ROOM:
			break;
		case PATIENT_TYPE:
			room.setPatientType((PatientType) value);
			break;
		case PATIENT_LASTNAME:
			room.setPatientLastName(((String) value).toUpperCase());
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
		case NURSE_FIRSTNAME:
			room.setNurseFirstName(((String) value).toUpperCase());
			break;
		}
	}

}
