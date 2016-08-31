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
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Professions;

public class Hermes implements Deity {

	@Override
	public String getName() {
		return "Hermes";
	}

	@Override
	public String getExplanation() {
		return getName() + " is the Messenger of the gods; god of commerce, thieves, eloquence and streets.";
	}

	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public List<String> getReasons() {
		return Arrays.asList(
				"As a priest of " + getName() + ", I want our commerce to bloom",
				"As a thief, I worship " + getName() + " for good luck",
				"As a trickster, I worship " + getName()
		);
	}

	@Override
	public int getReasonIndex(WorldObject performer, World world) {
		if (performer.getProperty(Constants.PROFESSION) == Professions.PRIEST_PROFESSION) {
			return 0;
		} else if (performer.getProperty(Constants.PROFESSION) == Professions.THIEF_PROFESSION) {
			return 1;
		} else if (performer.getProperty(Constants.PROFESSION) == Professions.TRICKSTER_PROFESSION) {
			return 2;
		}
		
		return -1;
	}
	
	@Override
	public void onTurn(World world, WorldStateChangedListeners creatureTypeChangedListeners) {
	}
	

	@Override
	public ImageIds getStatueImageId() {
		return ImageIds.STATUE_OF_HERMES;
	}
	
	@Override
	public SkillProperty getSkill() {
		return Constants.LIGHT_ARMOR_SKILL;
	}
	
	@Override
	public ImageIds getBoonImageId() {
		return ImageIds.HERMES_SYMBOL;
	}
}
