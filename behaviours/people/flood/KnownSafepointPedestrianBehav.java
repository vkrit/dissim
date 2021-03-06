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

package behaviours.people.flood;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

import osm.Osm;
import util.HexagonalGrid;
import util.Point;
import util.Scenario;
import util.jcoord.LatLng;
import agents.AgentUtils;
import agents.people.PedestrianAgent;
import behaviours.QueryGridBehav;
import behaviours.people.PedestrianBehav;
import behaviours.people.PedestrianUtils;
import behaviours.people.YouAreDeadException;
import behaviours.people.YouAreSafeException;

/**
 * {@link Behaviour} that extends {@link PedestrianBehav} and chooses the
 * {@link Point} from adjacents that is nearest to a previously known safepoint.
 * If it isn't accessible then calls the choose method from
 * {@link SafepointPedestrianBehav}.
 * 
 * @author Alejandro Blanco, Manuel Gomar
 * 
 */
@SuppressWarnings("serial")
public class KnownSafepointPedestrianBehav extends PedestrianBehav {

	private Set<Point> objectives = null;
	private SafepointPedestrianBehav fallback;
	private LinkedList<Point> lastMovements = new LinkedList<Point>();

	/**
	 * {@link KnownSafepointPedestrianBehav} constructor
	 * 
	 * @param args
	 *            The array must contain an {@link Agent} (owner of the
	 *            behaviour, usually a {@link PedestrianAgent}), an Environment
	 *            {@link AID} (initial environment), a {@link Scenario}, a
	 *            {@link Double} (latitude), a {@link Double} (longitude), a
	 *            {@link Integer} (distance of vison in tiles) and a
	 *            {@link Integer} (speed in tiles).
	 */
	public KnownSafepointPedestrianBehav(Object[] args) {
		super(args);
	}

	/**
	 * It chooses where to move from a {@link Set} of adjacents {@link Point}.
	 * 
	 * @param adjacents
	 *            {@link Set}<{@link Point}>
	 * @return
	 * @throws YouAreDeadException
	 *             When the agent dies
	 * @throws YouAreSafeException
	 *             When the agent reaches a safepoint
	 */
	@Override
	protected Point choose(Set<Point> adjacents) throws YouAreDeadException,
			YouAreSafeException {
		// Si los objetivos aún no se han pasado a tiles abortamos
		if (objectives == null)
			return null;

		LinkedList<Point> result = new LinkedList<Point>();

		if (position != null) {
			adjacents = PedestrianUtils.filterByStreetView(adjacents, position);
			adjacents.add(position);

			// Si ya está en un refugio no se mueve ni muere ni nada
			if (Osm.getGenericType(position.getS()) == Osm.SafePoint)
				throw new YouAreSafeException(position);
		}

		// Separar casillas inundadas de las secas, y buscar refugios
		Set<Point> water = new HashSet<Point>(adjacents.size());
		Set<Point> dry = new HashSet<Point>(adjacents.size());
		Set<Point> safe = new HashSet<Point>(adjacents.size());
		for (Point pt : adjacents) {
			if (pt.getW() > 0)
				water.add(pt);
			else if (Osm.getGenericType(pt.getS()) == Osm.SafePoint)
				safe.add(pt);
			else if (Osm.getGenericType(pt.getS()) == Osm.Roads)
				dry.add(pt);
			// Las casillas que no son calles ni refugios se ignoran
		}

		if (PedestrianUtils.detectFloodDeath(dry, position))
			throw new YouAreDeadException("Surrounded by water :(");

		// Primera ejecución
		if (position == null)
			return PedestrianUtils.nearInSetToSet(dry, objectives);

		if (objectives.size() > 0 || safe.size() > 0) {
			if (safe.size() == 0) {
				// No hay ningún refugio a la vista
				int best = Integer.MIN_VALUE;
				int distPos = Integer.MAX_VALUE;
				boolean calcDistPos = true;

				// Puntuamos cada punto
				for (Point pt : dry) {
					int objective = Integer.MAX_VALUE;
					// Buscamos el objetivo más cercano y la distancia al mismo
					for (Point obj : objectives) {
						int dist = HexagonalGrid.distance(pt, obj);
						if (dist < objective)
							objective = dist;

						if (calcDistPos) {
							dist = HexagonalGrid.distance(position, obj);
							if (dist < distPos)
								distPos = dist;
						}
					}
					calcDistPos = false;

					int wasser = d;
					// Distancia a las casillas inundadas
					for (Point wpt : water)
						wasser += HexagonalGrid.distance(pt, wpt);
					if (water.size() != 0)
						wasser /= water.size();

					int score = score(pt, distPos, objective, wasser,
							position.getZ(), (int) pt.getZ());

					LinkedList<Point> aux = PedestrianUtils.accessible(
							adjacents, position, pt, s);

					if (score > best && aux.size() > 0) {
						best = score;
						result = aux;
					}
				}

			} else {
				// Hay refugio a la vista
				LinkedList<Point> sortedSafe = new LinkedList<Point>();
				for (Point pt : safe) {
					// Ordenamos los refugios por distancia
					if (sortedSafe.size() == 0) {
						sortedSafe.add(pt);
					} else {
						ListIterator<Point> it = sortedSafe.listIterator();
						int d = HexagonalGrid.distance(position, pt);
						while (it.hasNext()) {
							Point spt = it.next();
							if (HexagonalGrid.distance(position, spt) > d) {
								it.previous();
								it.add(pt);
								break;
							} else if (!it.hasNext()) {
								// Está más lejos que todos los de la lista
								// hasta ese momento
								it.add(pt);
							}
						}
					}
				}

				// Buscamos el refugio más cercano que esté accesible
				for (Point pt : sortedSafe) {
					result = PedestrianUtils.accessible(adjacents, position,
							pt, s);
					if (result.size() > 0)
						break;
				}
			}
		}

		if (result.size() == 0) {
			// En el caso en que no sepa donde hay refugios o no sea posible
			// acceder a ninguno
			try {
				result = PedestrianUtils.accessible(adjacents, position,
						fallback.choose(adjacents), s);
			} catch (Exception e) {
				result = new LinkedList<Point>();
			}
		}

		if (result.size() > 0)
			lastMovements.addAll(result);
		while (lastMovements.size() > d)
			lastMovements.removeFirst();

		if (result.size() > 0)
			return result.getLast();
		else
			return null;
	}

	/**
	 * Returns the score of the tile
	 * 
	 * @param p
	 *            {@link Point} to score
	 * @param distancePosition
	 *            Distance from the position of the agent to the nearest
	 *            objective (reference)
	 * @param objective
	 *            Distance to the nearest objective (less is better)
	 * @param water
	 *            Distance to water (more is better)
	 * @param elevation
	 *            Elevation of the tile (more is better)
	 * @return
	 */
	private int score(Point p, float distancePosition, float objective,
			float water, float elevationPosition, float elevation) {
		float obj = ((distancePosition - objective) * 100.0F) / d;
		obj *= 1.8; // 80% más de importancia a llegar al objetivo

		float wat = (water * 100.0F) / d;
		wat *= 1.2; // 20% más de importancia a alejarse del agua

		float aux = elevation - elevationPosition;
		float aux2 = elevationPosition / 100.0F;
		float elev = aux;
		if (aux2 != 0)
			elev = aux / aux2;

		// Penalizamos las casillas por las que acabamos de pasar
		int penal = 0;
		int count = 0;
		for (Point pt : lastMovements) {
			count++;
			if (p.equals(pt)) {
				penal = (300 / (d / 2)) * count;
				break;
			}
		}

		return (int) (obj + wat + elev - penal);
	}

	/**
	 * The array must contain a {@link Set}<{@link LatLng}> with the
	 * geographical coordinates of the previously known safepoints.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void chooseArgs(Object[] args) {
		Set<LatLng> geoObj = (Set<LatLng>) args[0];
		if (geoObj == null) // Evitar que salte un null pointer
			geoObj = new HashSet<LatLng>(0);
		myAgent.addBehaviour(new GeoToPosBehav(myAgent, geoObj));
	}

	/**
	 * Sets the objectives as a set of grid coordinates of the previously known
	 * safepoints.
	 * 
	 * @param posObj
	 *            {@link Set}<{@link Point}>
	 * @param behav
	 *            {@link GeoToPosBehav}
	 */
	protected void setPosObjective(Set<Point> posObj, GeoToPosBehav behav) {
		objectives = posObj;
		myAgent.removeBehaviour(behav);
	}

	/**
	 * {@link Behaviour} that converts a {@link Set} of geographical coordinates
	 * on a {@link Set} of grid coordinates.
	 * 
	 * @author Alejandro Blanco, Manuel Gomar
	 * 
	 */
	protected class GeoToPosBehav extends CyclicBehaviour {

		/**
		 * {@link Set}<{@link Point}> with the grid coordinates
		 */
		private Set<Point> posObjGtP;
		private int stepGtP = 0;
		/**
		 * {@link Iterator}<{@link LatLng}> of the geographical coordinates to
		 * convert
		 */
		private Iterator<LatLng> itGtP;
		private MessageTemplate mtGtP;
		private DFAgentDescription[] envsGtP = null;

		/**
		 * {@link GeoToPosBehav} constructor
		 * 
		 * @param agt
		 *            Usually a {@link PedestrianAgent}
		 * @param geoObj
		 *            {@link Set}<{@link LatLng}> of geographical coordinates to
		 *            convert into grid coordinates.
		 */
		public GeoToPosBehav(Agent agt, Set<LatLng> geoObj) {
			super(agt);
			posObjGtP = new HashSet<Point>(geoObj.size());
			itGtP = geoObj.iterator();
		}

		@Override
		public void action() {
			switch (stepGtP) {
			case 0:
				if (itGtP.hasNext()) {
					LatLng obj = itGtP.next();

					String env = Integer.toString(scen
							.getEnviromentByCoord(obj));
					// Obtener agente entorno
					if (envsGtP == null)
						envsGtP = AgentUtils.search(myAgent, "grid-querying");
					AID envAID = null;
					for (DFAgentDescription df : envsGtP) {
						String name = df.getName().getLocalName();
						name = name.substring(name.indexOf("-") + 1,
								name.lastIndexOf("-"));
						if (name.equals(env)) {
							envAID = df.getName();
							break;
						}
					}

					String content = QueryGridBehav.COORD_TO_TILE + " "
							+ obj.getLat() + " " + obj.getLng();
					mtGtP = AgentUtils.send(myAgent, envAID,
							ACLMessage.REQUEST, "query-grid", content);
					stepGtP = 1;
				} else {
					// Hemos terminado!
					setPosObjective(posObjGtP, this);
					stepGtP = -1;
					return;
				}
				break;
			case 1:
				ACLMessage msg = myAgent.receive(mtGtP);
				if (msg != null) {
					String[] data = msg.getContent().split(" ");
					Point p = new Point(Integer.parseInt(data[0]),
							Integer.parseInt(data[1]));
					posObjGtP.add(p);
					stepGtP = 0;
				} else {
					block();
				}
				break;
			default:
				return;
			}
		}

	}

}
