/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower.deity;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

public interface Deity extends Serializable {

	public String getName();
	public String getExplanation();
	public List<String> getReasons();
	public int getReasonIndex(WorldObject performer, World world);
	public void onTurn(World world);
	
	public static final Demeter DEMETER = new Demeter();
	public static final Hephaestus HEPHAESTUS = new Hephaestus();
	public static final Hades HADES = new Hades();
	public static final Aphrodite APHRODITE = new Aphrodite();
	public static final Apollo APOLLO = new Apollo();
	public static final Dionysus DIONYSUS = new Dionysus();
	public static final Ares ARES = new Ares();
	public static final Artemis ARTEMIS = new Artemis();
	public static final Hermes HERMES = new Hermes();
	public static final Athena ATHENA = new Athena();
	
	public static final List<Deity> ALL_DEITIES = Arrays.asList(
			DEMETER,
			HEPHAESTUS,
			HADES,
			APHRODITE,
			APOLLO,
			DIONYSUS,
			ARES,
			ARTEMIS,
			HERMES,
			ATHENA
			);

	public static List<String> getAllDeityNames() {
		return ALL_DEITIES.stream().map(deity -> deity.getName()).collect(Collectors.toList());
	}
	
	//For now, default implementation
	public default List<Goal> getOrganizationGoals() {
		return Arrays.asList(Goals.DESTROY_SHRINES_TO_OTHER_DEITIES_GOAL);
	}
	
	public default List<String> getOrganizationGoalDescriptions() {
		return getOrganizationGoals().stream().map(g -> g.getDescription()).collect(Collectors.toList());
	}
	
	public default Object readResolveImpl() throws ObjectStreamException {
		Class<?> clazz = getClass();
		
		for(Deity deity : ALL_DEITIES) {
			if (deity.getClass() == clazz) {
				return deity;
			}
		}
		
		throw new IllegalStateException("Profession with class " + clazz + " not found");
	}
	public static List<String> getNames() {
		return ALL_DEITIES.stream().map(d -> d.getName()).collect(Collectors.toList());
	}
	public static Deity getDeityByDescription(String deityName) {
		for(Deity deity : ALL_DEITIES) {
			if (deity.getName().equals(deityName)) {
				return deity;
			}
		}
		throw new IllegalStateException("deity " + deityName + " not found in " + ALL_DEITIES);
	
	}
}
