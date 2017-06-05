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

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.Goals;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Professions;

public class Ares implements Deity {

	@Override
	public String getName() {
		return "Ares";
	}

	@Override
	public String getExplanation() {
		return getName() + " is the God of war, bloodshed, and violence.";
	}

	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public List<String> getReasons() {
		return Arrays.asList(
				"As a priest of " + getName() + ", I want our troops to win any war they enter"
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
	public void onTurn(World world, WorldStateChangedListeners worldStateChangedListeners) {
		if (DeityRetribution.shouldCheckForDeityRetribution(this, world)) { 
			if (DeityPropertyUtils.deityIsUnhappy(this, world)) {
				int x = world.getCurrentTurn().getValue() % world.getWidth();
				int y = world.getCurrentTurn().getValue() % world.getHeight();
				
				if (GoalUtils.isNonWaterOpenSpace(x, y, 1, 1, world)) {
					CreatureGenerator.generateMinotaur(x, y, world);
					world.getWorldStateChangedListeners().deityRetributed(this, getName() + " is displeased due to lack of followers and worship and caused a minotaur to appear");
				}
			}
		}
	}

	@Override
	public List<Goal> getOrganizationGoals() {
		return addDefaultOrganizationGoals(Goals.SACRIFICE_PEOPLE_TO_DEITY_GOAL);
	}

	@Override
	public ImageIds getStatueImageId() {
		return ImageIds.STATUE_OF_ARES;
	}
	
	@Override
	public SkillProperty getSkill() {
		return Constants.HAND_TO_HAND_SKILL;
	}
	
	@Override
	public ImageIds getBoonImageId() {
		return ImageIds.ARES_SYMBOL;
	}
	
	@Override
	public Condition getBoon() {
		return Condition.ARES_BOON_CONDITION;
	}
	
	@Override
	public String getBoonDescription() {
		return getName() + "'s boon: grants a bonus to melee attacks";
	}
}
