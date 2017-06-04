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
import org.worldgrower.condition.WerewolfUtils;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Professions;

public class Artemis implements Deity {

	@Override
	public String getName() {
		return "Artemis";
	}

	@Override
	public String getExplanation() {
		return getName() + " is the Goddess of the hunt, virginity, archery, the moon, and all animals.";
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
	public void onTurn(World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		if (DeityRetribution.shouldCheckForDeityRetribution(this, world)) { 
			if (DeityPropertyUtils.deityIsUnhappy(this, world) && (WerewolfUtils.getWerewolfCount(world) == 0)) {
				cursePersonAsWerewolf(world);
			}
		}
	}

	private void cursePersonAsWerewolf(World world) {
		List<WorldObject> targets = DeityPropertyUtils.getWorshippersFor(Deity.APHRODITE, world);
		targets = targets.stream().filter(w -> WerewolfUtils.canBecomeWerewolf(w)).collect(Collectors.toList());
		final WorldObject target;
		if (targets.size() > 0) {
			target = targets.get(0);
		} else {
			target = null;
		}
		
		if (target != null) {
			WerewolfUtils.makePersonIntoWerewolf(target, world);
			
			world.getWorldStateChangedListeners().deityRetributed(this, getName() + " is displeased due to lack of followers and worship and caused a person to turn into a werewolf");
		}
	}
	
	@Override
	public ImageIds getStatueImageId() {
		return ImageIds.STATUE_OF_ARTEMIS;
	}
	
	@Override
	public SkillProperty getSkill() {
		return Constants.ARCHERY_SKILL;
	}
	
	@Override
	public ImageIds getBoonImageId() {
		return ImageIds.ARTEMIS_SYMBOL;
	}
	
	@Override
	public Condition getBoon() {
		return Condition.ARTEMIS_BOON_CONDITION;
	}
	
	@Override
	public String getBoonDescription() {
		return getName() + "'s boon: grants a bonus to ranged attacks";
	}
}
