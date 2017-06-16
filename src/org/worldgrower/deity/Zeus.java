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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.curse.Curse;
import org.worldgrower.goal.Goals;
import org.worldgrower.goal.MatePropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.personality.PersonalityTrait;
import org.worldgrower.profession.Professions;

public class Zeus implements Deity {

	@Override
	public String getName() {
		return "Zeus";
	}

	@Override
	public String getExplanation() {
		return getName() + " is the God of the sky, lightning, thunder, law, order and justice";
	}

	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public List<String> getReasons() {
		return Arrays.asList(
				"As a priest of " + getName() + ", I want to honor the king of the Gods",
				"I worship " + getName() +" because I want to uphold the law"
		);
	}

	@Override
	public int getReasonIndex(WorldObject performer, World world) {
		if (performer.getProperty(Constants.PROFESSION) == Professions.PRIEST_PROFESSION) {
			return 0;
		} else if (performer.getProperty(Constants.PROFESSION) == Professions.SHERIFF_PROFESSION) {
			return 1;
		}
		
		return -1;
	}
	
	@Override
	public int getOrganizationGoalIndex(WorldObject performer, World world) {
		if (performer.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.POWER_HUNGRY) > 0) {
			return getOrganizationGoals().indexOf(Goals.SWITCH_DEITY_GOAL);
		}
		return -1;
	}
	
	@Override
	public void onTurn(World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		if (DeityRetribution.shouldCheckForDeityRetribution(this, world)) { 
			if (DeityPropertyUtils.deityIsUnhappy(this, world)) {
				punishUnlawfulPerson(world);
			}
		}
	}

	private void punishUnlawfulPerson(World world) {
		List<HistoryItem> historyItems = new ArrayList<>();
		historyItems.addAll(world.getHistory().findHistoryItems(Actions.STEAL_ACTION));
		historyItems.addAll(world.getHistory().findHistoryItems(Actions.STEAL_GOLD_ACTION));
		historyItems = historyItems.stream().filter(h -> h.getTurn().getValue() > world.getCurrentTurn().getValue() - 10 && h.getPerformer().getProperty(Constants.CURSE) == null).collect(Collectors.toList());

		final WorldObject target;
		if (historyItems.size() > 0) {
			target = historyItems.get(0).getPerformer();
		} else {
			target = null;
		}
		
		if (target != null) {
			target.setProperty(Constants.CURSE, Curse.POX_CURSE);			
			world.getWorldStateChangedListeners().deityRetributed(this, getName() + " is displeased due to lack of followers and worship and caused a person to become cursed");
		}
	}

	@Override
	public ImageIds getStatueImageId() {
		return ImageIds.STATUE_OF_ZEUS;
	}
	
	@Override
	public SkillProperty getSkill() {
		return Constants.TRANSMUTATION_SKILL;
	}
	
	@Override
	public ImageIds getBoonImageId() {
		return ImageIds.ZEUS_SYMBOL;
	}
	
	@Override
	public Condition getBoon() {
		return Condition.ZEUS_BOON_CONDITION;
	}
	
	@Override
	public String getBoonDescription() {
		return getName() + "'s boon: grants a bonus to lightning magic spells";
	}
}
