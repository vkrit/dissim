package test;

import osm.GetOSMInfo;
import util.Scenario;
import util.flood.FloodScenario;
import util.jcoord.LatLng;

public class OSMgetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url="http://api.openstreetmap.org/api/0.6/map?bbox=11.54,48.14,11.543,48.145";
		
		Scenario newOrleans = new FloodScenario();
		newOrleans.setGeoData(new LatLng(29.953260, -90.088238, (short)10), new LatLng(
				29.918075, -90.053707, (short)10), (short) 600);
		newOrleans.setName("Position Grid Test");
		newOrleans.setDescription("NW SE 1m");
		newOrleans.complete();
		
		GetOSMInfo osmInfo = new GetOSMInfo(newOrleans.getArea());
		
	}
	
	
 
}
