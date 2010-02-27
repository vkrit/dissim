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

package agents.flood;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import util.jcoord.LatLng;
import behaviours.ReceiveScenarioBehav;
import behaviours.RequestScenarioBehav;
import behaviours.flood.WaterSourceBehav;

public class WaterSourceAgent extends Agent {

	private static final long serialVersionUID = -901992561566307027L;
	private AID envAID;
	private LatLng coord;
	private short water;
	private long rhythm;

	@Override
	protected void setup() {
		// Obtener argumentos
		Object[] args = getArguments();
		if (args.length == 4) {
			double lat = Double.parseDouble((String) args[0]);
			double lng = Double.parseDouble((String) args[1]);
			coord = new LatLng(lat, lng);
			water = Short.parseShort((String) args[2]);
			rhythm = Long.parseLong((String) args[3]);
		} else {
			throw new IllegalArgumentException("Wrong arguments.");
		}

		addBehaviour(new RequestScenarioBehav(new ContinueWS()));
	}

	@SuppressWarnings("serial")
	protected class ContinueWS extends ReceiveScenarioBehav {

		boolean done = false;

		@Override
		public void action() {
			String env = Integer.toString(scen.getEnviromentByCoord(coord));

			// Obtener agentes entorno
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("add-water");
			template.addServices(sd);
			try {
				DFAgentDescription[] result = DFService.search(myAgent,
						template);
				if (result.length < 1)
					throw new Exception(
							"Error searching for the enviroment agent. Found "
									+ result.length + " agents.");
				for (DFAgentDescription df : result) {
					String name = df.getName().getLocalName();
					name = name.substring(name.indexOf("-") + 1, name
							.lastIndexOf("-"));
					if (name.equals(env)) {
						envAID = df.getName();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				doDelete();
			}

			myAgent.addBehaviour(new WaterSourceBehav(myAgent, rhythm, envAID,
					coord, water));
			done = true;
		}

		@Override
		public boolean done() {
			return done;
		}

	}

}
