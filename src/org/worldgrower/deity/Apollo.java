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
import org.worldgrower.condition.CreatureTypeChangedListeners;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Professions;

public class Apollo implements Deity {

	@Override
	public String getName() {
		return "Apollo";
	}

	@Override
	public String getExplanation() {
		return getName() + " is the God of music, arts, knowledge, healing, plague, prophecy, poetry, manly beauty, archery, and the sun.";
	}

	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public List<String> getReasons() {
		return Arrays.asList(
				"I worship " + getName() + " to strike true with my archery skills",
				"I worship " + getName() + " to have good weather to work in",
				getName() + " is the god of arts, and as his priest I do everything to promote the arts"
		);
	}

	@Override
	public int getReasonIndex(WorldObject performer, World world) {
		if (performer.getProperty(Constants.ARCHERY_SKILL).getLevel() > 0) {
			return 0;
		} else if (performer.getProperty(Constants.PROFESSION) == Professions.LUMBERJACK_PROFESSION) {
			return 1;
		} else if (performer.getProperty(Constants.PROFESSION) == Professions.PRIEST_PROFESSION) {
			return 2;
		}
		
		return -1;
	}
	
	@Override
	public void onTurn(World world, CreatureTypeChangedListeners creatureTypeChangedListeners) {
	}

	@Override
	public List<Goal> getOrganizationGoals() {
		return Arrays.asList(Goals.DESTROY_SHRINES_TO_OTHER_DEITIES_GOAL, Goals.HEAL_OTHERS_GOAL);
	}
	

	@Override
	public ImageIds getStatueImageId() {
		return ImageIds.STATUE_OF_APOLLO;
	}
	
	@Override
	public void worship(WorldObject performer, WorldObject target, int worshipCount, World world) {
		if (worshipCount == 5) {
			performer.getProperty(Constants.RESTORATION_SKILL).use(30);
		}
	}
}
