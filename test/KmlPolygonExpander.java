package test;

import googleEarth.GoogleEarthFlood;
import gui.VisorFrame;
import util.Scenario;
import util.flood.FloodHexagonalGrid;
import util.flood.FloodScenario;
import util.jcoord.LatLng;

public class KmlPolygonExpander {
	public static void main(String[] args) throws InterruptedException {
		Scenario newOrleans = new FloodScenario();

		// newOrleans.setArea(new LatLng(29.953260, -90.088238), new LatLng(
		// 29.918075, -90.053707));
		newOrleans.setGeoData(new LatLng(29.953260, -90.088238, (short) 10),
				new LatLng(29.918075, -90.053707, (short) 10), (short) 300);
		newOrleans.setName("Polygon Expander TEST");
		newOrleans.setDescription("NW SE 1m");
		newOrleans.setDateAndTime(2000, 3, 15, 15, 3);
		newOrleans.complete();

		System.out.println(newOrleans.toString());

		FloodHexagonalGrid grid = (FloodHexagonalGrid) newOrleans.getGrid();

		GoogleEarthFlood k = new GoogleEarthFlood(newOrleans.getName(),newOrleans.getDescription());

		for (int i = 0; i < newOrleans.getGridSize()[0]; i++) {
			for (int j = 0; j < newOrleans.getGridSize()[1]; j++) {
				short x = (short) ((Math.random() * 100) % 2);
				grid.setTerrainValue(i, j, x);
			}
		}

		VisorFrame v1 = new VisorFrame();
		v1.update(grid);
		v1.setVisible(true);

		k.update(newOrleans);
	}
}