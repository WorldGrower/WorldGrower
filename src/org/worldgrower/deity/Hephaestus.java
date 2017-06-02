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
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Professions;

public class Hephaestus implements Deity {

	@Override
	public String getName() {
		return "Hephaestus";
	}

	@Override
	public String getExplanation() {
		return getName() + " is the crippled god of fire, metalworking, and crafts.";
	}

	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public List<String> getReasons() {
		return Arrays.asList(
				"I worship " + getName() + " because I'm a blacksmith",
				"I worship " + getName() + " because I'm a miner",
				getName() + " is the God of crafts and as his priest I promote the crafts through our community"
		);
	}

	@Override
	public int getReasonIndex(WorldObject performer, World world) {
		if (performer.getProperty(Constants.PROFESSION) == Professions.BLACKSMITH_PROFESSION) {
			return 0;
		} else if (performer.getProperty(Constants.PROFESSION) == Professions.MINER_PROFESSION) {
			return 1;
		} else if (performer.getProperty(Constants.PROFESSION) == Professions.PRIEST_PROFESSION) {
			return 2;
		}
		
		return -1;
	}
	
	@Override
	public void onTurn(World world, WorldStateChangedListeners worldStateChangedListeners) {
		if (DeityRetribution.shouldCheckForDeityRetribution(this, world)) { 
			if (DeityPropertyUtils.deityIsUnhappy(this, world)) {
				DeityPropertyUtils.destroyResource(Constants.ORE_SOURCE, this, getName() + " is displeased due to lack of followers and worship and destroyed a mine", world);
			}
		}
	}

	@Override
	public ImageIds getStatueImageId() {
		return ImageIds.STATUE_OF_HEPHAESTUS;
	}
	
	@Override
	public SkillProperty getSkill() {
		return Constants.SMITHING_SKILL;
	}
	
	@Override
	public ImageIds getBoonImageId() {
		return ImageIds.HEPHAESTUS_SYMBOL;
	}
	
	@Override
	public Condition getBoon() {
		return Condition.HEPHAESTUS_BOON_CONDITION;
	}
	
	@Override
	public String getBoonDescription() {
		return getName() + "'s boon: grants a bonus to smithing";
	}
}
