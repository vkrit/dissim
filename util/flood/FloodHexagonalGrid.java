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

package util.flood;

import java.util.ArrayList;

import util.HexagonalGrid;

public class FloodHexagonalGrid extends HexagonalGrid {

	protected short[][] gridWater; // Nivel de agua en la casilla
	protected boolean useGridMod;
	protected boolean[][] gridMod;

	public FloodHexagonalGrid(int x, int y, boolean useAgents) {
		super(x, y);
		gridWater = new short[x][y];
		useGridMod = !useAgents;
		if (useGridMod)
			gridMod = new boolean[x][y];
	}

	public short setWaterValue(int x, int y, short value) {
		short old = gridWater[x][y];
		gridWater[x][y] = value;
		return old;
	}

	@Override
	public short increaseValue(int x, int y, short increment) {
		// TODO A la hora de inundar el agua se pone al nivel del resto, de
		// manera que la capa superior sea uniforme
		// double offset = 0;
		// double value = getValue(x, y);
		// int aux = (int) value;
		// offset = value - ((double) aux);

		gridWater[x][y] += increment;

		if (useGridMod)
			gridMod[x][y] = true;

		printGrid(); // TODO Debug
		return 0;
	}

	@Override
	public short decreaseValue(int x, int y, short decrement) {
		short result;
		// El nivel de agua no puede ser menor que cero
		if (gridWater[x][y] >= decrement) {
			gridWater[x][y] -= decrement;
			result = decrement;
		} else {
			result = gridWater[x][y];
			gridWater[x][y] = 0;
		}

		if (useGridMod)
			gridMod[x][y] = true;
		return result;
	}

	@Override
	public short getValue(int x, int y) {
		return (short) (gridTerrain[x][y] + gridWater[x][y]);
	}

	public short getWaterValue(int x, int y) {
		return gridWater[x][y];
	}

	public ArrayList<int[]> getModCoordAndReset() {
		if (!useGridMod)
			return null;

		ArrayList<int[]> result = new ArrayList<int[]>();
		for (int i = 0; i < gridMod.length; i++) {
			for (int j = 0; j < gridMod[i].length; j++) {
				if (gridMod[i][j]) {
					result.add(new int[] { i, j });
					gridMod[i][j] = false;
				}
			}
		}
		return result;
	}

}
