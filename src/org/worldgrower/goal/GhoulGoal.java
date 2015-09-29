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

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.GhoulUtils;
import org.worldgrower.creaturetype.CreatureType;

public class GhoulGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		int indexOfMeat = performerInventory.getIndexFor(w -> isHumanMeat(w));
		if (indexOfMeat != -1 && !performerInventory.get(indexOfMeat).getProperty(Constants.SELLABLE)) {
			return new OperationInfo(performer, performer, new int[] {indexOfMeat}, Actions.MARK_INVENTORY_ITEM_AS_SELLABLE_ACTION);
		} else if (performer.getProperty(Constants.HIT_POINTS) > 1) {
			return new OperationInfo(performer, performer, new int[0], Actions.CREATE_HUMAN_MEAT_ACTION);
		}
		return null;
	}
	
	private boolean isHumanMeat(WorldObject w) {
		return w.hasProperty(Constants.FOOD) && w.hasProperty(Constants.CREATURE_TYPE) && w.getProperty(Constants.CREATURE_TYPE) == CreatureType.HUMAN_CREATURE_TYPE;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<WorldObject> ghouls = findGhouls(performer, world);
		return ghouls.size() > 0;
	}

	private List<WorldObject> findGhouls(WorldObject performer, World world) {
		return world.findWorldObjectsByProperty(Constants.STRENGTH, w -> GhoulUtils.isGhoul(w));
	}

	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "creating ghouls";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return findGhouls(performer, world).size();
	}
}
