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
package org.worldgrower.goal;

import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Condition;
import org.worldgrower.deity.Deity;

public class SacrificePeopleToDeityGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> sacrificialAltars = SacrificeUtils.getSacrificialAltars(performer, world);
		if (sacrificialAltars.size() > 0) {
			WorldObject personOnAltar = getPersonOnAltars(sacrificialAltars, world);
			if (personOnAltar != null) {
				return new OperationInfo(performer, personOnAltar, new int[0], Actions.MELEE_ATTACK_ACTION);
			} else {
				List<WorldObject> peopleToSacrifice = getPeopleToSacrifice(performer, world);
				if (peopleToSacrifice.size() > 0) {
					List<WorldObject> unconsciousPeople = peopleToSacrifice.stream().filter(w -> w.getProperty(Constants.CONDITIONS).hasCondition(Condition.UNCONSCIOUS_CONDITION)).collect(Collectors.toList());
					if (unconsciousPeople.size() > 0) {
						return new OperationInfo(performer, unconsciousPeople.get(0), new int[0], Actions.CAPTURE_PERSON_FOR_SACRIFICE_ACTION);
					} else {
						return new OperationInfo(performer, peopleToSacrifice.get(0), new int[0], Actions.NON_LETHAL_MELEE_ATTACK_ACTION);
					}
				} else {
					return null;
				}
			}
		} else {
			WorldObject target = BuildLocationUtils.findOpenLocationAwayFromExistingProperty(performer, 1, 2, world);
			return new OperationInfo(performer, target, new int[0], Actions.BUILD_SACRIFICAL_ALTAR_ACTION);
		}
	}
	
	private WorldObject getPersonOnAltars(List<WorldObject> sacrificialAltars, World world) {
		for(WorldObject sacrificialAltar : sacrificialAltars) {
			int altarX = sacrificialAltar.getProperty(Constants.X);
			int altarY = sacrificialAltar.getProperty(Constants.Y);
			List<WorldObject> personsOnAltar = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.getProperty(Constants.X) == altarX && w.getProperty(Constants.Y) == altarY);
			if (personsOnAltar.size() > 0) {
				return personsOnAltar.get(0);
			}
		}
		return null;
	}

	private List<WorldObject> getPeopleToSacrifice(WorldObject performer, World world) {
		Deity performerDeity = performer.getProperty(Constants.DEITY);
		return world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.getProperty(Constants.DEITY) != performerDeity);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return getPeopleToSacrifice(performer, world).size() == 0;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "sacrifiving people to a deity";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return Integer.MAX_VALUE - getPeopleToSacrifice(performer, world).size();
	}
}
