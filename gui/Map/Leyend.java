//    Flood and evacuation simulator using multi-agent technology
//    Copyright (C) 2010 Alejandro Blanco and Manuel Gomar
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package gui.Map;

import javax.swing.table.AbstractTableModel;

public class Leyend extends AbstractTableModel {
	/**
	 * Autogenerated serial version
	 */
	private static final long serialVersionUID = 434944410240463493L;
	String[] columnNames;
	Object[][] data;

	public Leyend(int key) {
		switch (key) {
		case 1:// ROAD_MAP
			columnNames = getMapColums();
			data = getMapData();
			break;
		default:
			break;
		}
	}

	private Object[][] getMapData() {
		Object[][] data = {
		// {"Raw Field", new Color(OsmMap.Raw_Field *1000), new Boolean(true)},
		// {"Highway", new Color(OsmMap.Highway *1000), new Boolean(true)},
		// {"Barrier", new Color(OsmMap.Barrier *1000), new Boolean(true)},
		// {"Cycleway", new Color(OsmMap.Cycleway *1000), new Boolean(true)},
		// {"Tracktype", new Color(OsmMap.Tracktype *1000), new Boolean(true)},
		// {"Waterway", new Color(OsmMap.Waterway *1000), new Boolean(true)},
		// {"Railway", new Color(OsmMap.Railway *1000), new Boolean(true)},
		// {"Aeroway", new Color(OsmMap.Aeroway *1000), new Boolean(true)},
		// {"Aerialway", new Color(OsmMap.Aerialway *1000), new Boolean(true)},
		// {"Power", new Color(OsmMap.Power *1000), new Boolean(true)},
		// {"Man Made", new Color(OsmMap.Man_Made *1000), new Boolean(true)},
		// {"Leisure", new Color(OsmMap.Leisure *1000), new Boolean(true)},
		// {"Amenity", new Color(OsmMap.Amenity *1000), new Boolean(true)},
		// {"Shop", new Color(OsmMap.Shop *1000), new Boolean(true)},
		// {"Tourism", new Color(OsmMap.Tourism *1000), new Boolean(true)},
		// {"Historic", new Color(OsmMap.Historic *1000), new Boolean(true)},
		// {"Landuse", new Color(OsmMap.Landuse *1000), new Boolean(true)},
		// {"Military", new Color(OsmMap.Military *1000), new Boolean(true)},
		// {"Natural", new Color(OsmMap.Natural *1000), new Boolean(true)},
		// {"Geological", new Color(OsmMap.Geological *1000), new
		// Boolean(true)},
		// {"Building", new Color(OsmMap.Building *1000), new Boolean(true)}
		};
		return data;
	}

	private String[] getMapColums() {
		String[] s = { "Type", "Color", "Show" };
		return s;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;

	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];

	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col < 2) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}
