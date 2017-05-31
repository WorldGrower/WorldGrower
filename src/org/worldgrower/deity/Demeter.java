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
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.personality.PersonalityTrait;
import org.worldgrower.profession.Professions;

public class Demeter implements Deity {

	@Override
	public String getName() {
		return "Demeter";
	}

	@Override
	public String getExplanation() {
		return getName() + " is the God of harvest, sacred laws and life and death.";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public List<String> getReasons() {
		return Arrays.asList(
				"As a farmer I worship " + getName() + " to have a good harvest",
				"As a child, I was always scared of food running out. I worship " + getName() + " so that it never happens again.",
				"Our existence depends on nature. That is why I worship " + getName()
		);
	}
	
	@Override
	public List<Goal> getOrganizationGoals() {
		return addDefaultOrganizationGoals(Goals.FEED_OTHERS_GOAL);
	}
	
	

	@Override
	public int getOrganizationGoalIndex(WorldObject performer, World world) {
		if (performer.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.GREEDY) < -500) {
			return getOrganizationGoals().indexOf(Goals.FEED_OTHERS_GOAL);
		}
		return -1;
	}

	@Override
	public int getReasonIndex(WorldObject performer, World world) {
		if (performer.getProperty(Constants.PROFESSION) == Professions.FARMER_PROFESSION) {
			return 0;
		} else if (wasHungry(performer, world)) {
			return 1;
		} else if (performer.getProperty(Constants.PROFESSION) == Professions.PRIEST_PROFESSION) {
			return 2;
		}
			
		return -1;
	}
	
	private boolean wasHungry(WorldObject performer, World world) {
		List<HistoryItem> eatActions = world.getHistory().findHistoryItems(performer, Actions.EAT_ACTION);
		
		for(HistoryItem historyItem : eatActions) {
			if (historyItem.getPerformer().getProperty(Constants.FOOD) < 500) {
				return true;
			}
		}
		
		List<HistoryItem> eatFromInventoryActions = world.getHistory().findHistoryItems(performer, Actions.EAT_FROM_INVENTORY_ACTION);
		for(HistoryItem historyItem : eatFromInventoryActions) {
			if (historyItem.getPerformer().getProperty(Constants.FOOD) < 500) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void onTurn(World world, WorldStateChangedListeners worldStateChangedListeners) {
		if (DeityPropertyUtils.shouldCheckForDeityRetribution(world)) { 
			if (DeityPropertyUtils.deityIsUnhappy(world, this)) {
				wiltBerryBushes(world);
			}
		}
	}

	private void wiltBerryBushes(World world) {
		List<WorldObject> foodSources = world.findWorldObjectsByProperty(Constants.FOOD_SOURCE, w -> !w.hasIntelligence());
		for(WorldObject foodSource : foodSources) {
			Conditions.add(foodSource, Condition.WILTING_CONDITION, 10, world);
		}
		
		world.getWorldStateChangedListeners().deityRetributed(this, getName() + " is displeased due to lack of followers and worship and caused berry bushes to wilt");
	}
	

	@Override
	public ImageIds getStatueImageId() {
		return ImageIds.STATUE_OF_DEMETER;
	}
	
	@Override
	public SkillProperty getSkill() {
		return Constants.FARMING_SKILL;
	}
	
	@Override
	public ImageIds getBoonImageId() {
		return ImageIds.DEMETER_SYMBOL;
	}
	
	@Override
	public Condition getBoon() {
		return Condition.DEMETER_BOON_CONDITION;
	}
	
	@Override
	public String getBoonDescription() {
		return getName() + "'s boon: grants a bonus to food harvesting";
	}
}
