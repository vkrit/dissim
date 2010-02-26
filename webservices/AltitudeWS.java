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

package webservices;

import java.util.List;

import javax.xml.ws.WebServiceException;

import util.jcoord.LatLng;
import webservices.gov.usgs.gisdata.xmlwebservices2.ElevationService;
import webservices.gov.usgs.gisdata.xmlwebservices2.ElevationServiceSoap;
import webservices.gov.usgs.gisdata.xmlwebservices2.GetAllElevationsResponse.GetAllElevationsResult;
import webservices.gov.usgs.gisdata.xmlwebservices2.GetElevationResponse.GetElevationResult;

//http://gisdata.usgs.gov/XMLWebServices2/Elevation_service.asmx?WSDL

public class AltitudeWS {

	private static ElevationServiceSoap service = null;

	private AltitudeWS() {
		// Inaccesible - Clase no instanciable
	}

	private static void init() {
		ElevationService serv = new ElevationService();
		service = serv.getElevationServiceSoap();
	}

	public static String getElevation(LatLng coord, String sourceLayer,
			String elevationUnits, boolean elevationOnly)
			throws WebServiceException {
		if (service == null)
			init();

		// coord.toWGS84(); // WGS 1984 projection

		String elevationOnlyStr;
		if (elevationOnly)
			elevationOnlyStr = "TRUE";
		else
			elevationOnlyStr = "FALSE";
		GetElevationResult res = service.getElevation(Double.toString(coord
				.getLng()), Double.toString(coord.getLat()), elevationUnits,
				sourceLayer, elevationOnlyStr);
		List<Object> results = res.getContent();

		if (results.size() != 1)
			throw new WebServiceException("Wrong results obtained -> "
					+ results.toString());

		return results.get(0).toString();
	}

	public static double getElevation(LatLng coord) throws WebServiceException {
		// -1.79769313486231E+308 means no valid values were found at that point
		double altitude = Double.MIN_VALUE;

		String result = getElevation(coord, "-1", "METERS", true);
		result = result.substring(8, result.length() - 9);
		altitude = Double.parseDouble(result);

		return altitude;
	}

	public static String getAllElevations(LatLng coord, String elevationUnits)
			throws WebServiceException {
		if (service == null)
			init();

		// coord.toWGS84(); // WGS 1984 projection

		GetAllElevationsResult res = service.getAllElevations(Double
				.toString(coord.getLng()), Double.toString(coord.getLat()),
				elevationUnits);
		List<Object> results = res.getContent();

		if (results.size() != 1)
			throw new WebServiceException("Wrong results obtained -> "
					+ results.toString());

		return results.get(0).toString();
	}

	public static String getAllElevations(LatLng coord)
			throws WebServiceException {
		return getAllElevations(coord, "METERS");
	}
}
