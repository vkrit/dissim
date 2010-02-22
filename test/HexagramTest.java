package test;

import kml.KmlWriter;
import kml.flood.FloodKml;
import util.Scenario;
import util.flood.FloodScenario;
import util.jcoord.LatLng;

public class HexagramTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scenario newOrleans = new FloodScenario();

		//newOrleans.setArea(new LatLng(29.953260, -90.088238), new LatLng(
		//		29.918075, -90.053707));

		LatLng NW = new LatLng(29.953260, -90.088238, (short)10);
		newOrleans.setGeoData(NW, new LatLng(
				29.918075, -90.053707, (short)10), (short) 150);
		newOrleans.setName("Hexagrams grid Test");
		newOrleans.setDescription("NW SE 1m");
		newOrleans.setDateAndTime(2000, 3, 15, 15, 3);
		newOrleans.complete();
		
		System.out.println(newOrleans.toString());
		KmlWriter k = new KmlWriter();
		
		for (int i=0;i<2;i++){
			for (int j=0;j<5;j++){
				LatLng c = newOrleans.tileToCoord(i, j);
				k.drawHexagon("HEX", c);
				System.out.println("["+i+","+j+"] Distancia a [0,0] ->"+NW.distance(c));
			}
		}
		k.drawHexagon("HEX",newOrleans.tileToCoord(6, 6));
		
		k.createKmzFile("Hexagrams grid Test");

	}

}