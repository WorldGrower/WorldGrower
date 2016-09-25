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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.gui.ImageIds;

/**
 * A Deity is an entity which can be worshiped and has represents aspects of reality.
 */
public interface Deity extends Serializable {

	public String getName();
	public String getExplanation();
	public List<String> getReasons();
	public int getReasonIndex(WorldObject performer, World world);
	public void onTurn(World world, WorldStateChangedListeners creatureTypeChangedListeners);
	public ImageIds getStatueImageId();
	
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
	public static final Zeus ZEUS = new Zeus();
	public static final Hera HERA = new Hera();
	public static final Poseidon POSEIDON = new Poseidon();
	
	public static final List<Deity> ALL_DEITIES = Arrays.asList(
			APHRODITE,
			APOLLO,
			ARES,
			ARTEMIS,
			ATHENA,
			DEMETER,
			DIONYSUS,
			HADES,
			HEPHAESTUS,
			HERA,
			HERMES,
			POSEIDON,
			ZEUS
			);

	public static List<String> getAllDeityNames() {
		return ALL_DEITIES.stream().map(deity -> deity.getName()).collect(Collectors.toList());
	}
	
	public static List<Deity> getAllSortedDeities() {
		List<Deity> sortedDeities = new ArrayList<>(ALL_DEITIES);
		Collections.sort(sortedDeities, new Comparator<Deity>() {

			@Override
			public int compare(Deity deity1, Deity deity2) {
				return deity1.getName().compareTo(deity2.getName());
			}
			
		});
		return sortedDeities;
	}
	
	//For now, default implementation
	public default List<Goal> getOrganizationGoals() {
		return addDefaultOrganizationGoals();
	}
	
	public default List<Goal> addDefaultOrganizationGoals(Goal... goals) {
		List<Goal> goalsList = new ArrayList<>(Arrays.asList(goals));
		goalsList.add(Goals.DESTROY_SHRINES_TO_OTHER_DEITIES_GOAL);
		goalsList.add(Goals.SWITCH_DEITY_GOAL);
		return goalsList;
	}
	
	//For now, default implementation
	public default int getOrganizationGoalIndex(WorldObject performer, World world) {
		return -1;
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

	public static Deity getDeityByDescription(String deityName) {
		for(Deity deity : ALL_DEITIES) {
			if (deity.getName().equals(deityName)) {
				return deity;
			}
		}
		throw new IllegalStateException("deity " + deityName + " not found in " + ALL_DEITIES);
	}
	
	public static List<String> getAllDeityExplanations() {
		List<String> deityDescriptions = new ArrayList<>();
		for(Deity deity : ALL_DEITIES) {
			deityDescriptions.add(deity.getExplanation());
		}
		return deityDescriptions;
	}
	
	public default void worship(WorldObject performer, WorldObject target, int worshipCount, World world) {
		if (worshipCount == 5) {
			SkillUtils.useSkill(performer, getSkill(), 30, world.getWorldStateChangedListeners());
			world.logAction(Actions.WORSHIP_DEITY_ACTION, performer, target, Args.EMPTY, performer.getProperty(Constants.NAME) + " gains a boost to " + getSkill().getName() + " skill due to worship.");
		} else if (worshipCount == 20) {
			Conditions.add(performer, Condition.DISEASE_IMMUNITY_CONDITION, Integer.MAX_VALUE, world);
			world.logAction(Actions.WORSHIP_DEITY_ACTION, performer, target, Args.EMPTY, performer.getProperty(Constants.NAME) + " gains immunity to disease due to worship.");
		} else if (worshipCount == 50) {
			Conditions.add(performer, getBoon(), Integer.MAX_VALUE, world);
			world.logAction(Actions.WORSHIP_DEITY_ACTION, performer, target, Args.EMPTY, performer.getProperty(Constants.NAME) + " gains a boon from " + getName());
		}
	}
	
	public abstract SkillProperty getSkill();
	public abstract ImageIds getBoonImageId();
	public abstract Condition getBoon();
	public abstract String getBoonDescription();
}
