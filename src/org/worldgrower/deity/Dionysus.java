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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.VampireUtils;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.personality.PersonalityTrait;
import org.worldgrower.profession.Professions;

public class Dionysus implements Deity {

	@Override
	public String getName() {
		return "Dionysus";
	}

	@Override
	public String getExplanation() {
		return getName() + " is the God of wine, parties and festivals, madness, chaos, drunkenness, drugs, and ecstasy.";
	}

	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public List<String> getReasons() {
		return Arrays.asList(
				getName() + " is the God of wine and festivals, he is important for keeping up morale in our community"
				);
	}

	@Override
	public int getReasonIndex(WorldObject performer, World world) {
		if (performer.getProperty(Constants.PROFESSION) == Professions.PRIEST_PROFESSION) {
			return 0;
		}
		return -1;
	}
	
	@Override
	public List<Goal> getOrganizationGoals() {
		return addDefaultOrganizationGoals(Goals.GHOUL_GOAL, Goals.START_DRINKING_CONTEST_GOAL);
	}
	
	@Override
	public int getOrganizationGoalIndex(WorldObject performer, World world) {
		if (performer.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.HONORABLE) > 0) {
			return getOrganizationGoals().indexOf(Goals.START_DRINKING_CONTEST_GOAL);
		} else {
			return getOrganizationGoals().indexOf(Goals.GHOUL_GOAL);
		}
	}

	@Override
	public void onTurn(World world, WorldStateChangedListeners worldStateChangedListeners) {
		
		if (DeityRetribution.shouldCheckForDeityRetribution(this, world)) {
			int totalNumberOfWorshippers = DeityPropertyUtils.getTotalNumberOfWorshippers(world);
			if (totalNumberOfWorshippers > 18 && DeityPropertyUtils.getWorshippersFor(this, world).size() > 0 && (VampireUtils.getVampireCount(world) == 0)) {
				cursePersonAsVampire(world, worldStateChangedListeners);
			}
		}
	}

	private void cursePersonAsVampire(World world, WorldStateChangedListeners worldStateChangedListeners) {
		List<WorldObject> targets = DeityPropertyUtils.getWorshippersFor(this, world);
		targets = targets.stream().filter(w -> VampireUtils.canBecomeVampire(w)).collect(Collectors.toList());
		if (targets.size() > 0) {
			VampireUtils.vampirizePerson(targets.get(0), worldStateChangedListeners);
		}
		
		world.getWorldStateChangedListeners().deityRetributed(this, getName() + " caused a person to become a vampire");
	}

	@Override
	public ImageIds getStatueImageId() {
		return ImageIds.STATUE_OF_DIONYSUS;
	}
	
	@Override
	public SkillProperty getSkill() {
		return Constants.ALCHEMY_SKILL;
	}
	
	@Override
	public ImageIds getBoonImageId() {
		return ImageIds.DIONYSUS_SYMBOL;
	}
	
	@Override
	public Condition getBoon() {
		return Condition.DIONYSUS_BOON_CONDITION;
	}
	
	@Override
	public String getBoonDescription() {
		return getName() + "'s boon: grants a bonus to grape harvesting and wine production";
	}
}
