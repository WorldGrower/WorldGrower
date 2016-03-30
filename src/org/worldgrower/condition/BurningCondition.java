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
package org.worldgrower.condition;

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.Item;

public class BurningCondition implements DeadlyCondition {

	public BurningCondition(List<Condition> allConditions) {
		allConditions.add(this);
	}

	@Override
	public boolean canTakeAction() {
		return true;
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public String getDescription() {
		return "burning";
	}

	@Override
	public void onTurn(WorldObject worldObject, World world, int startTurn, WorldStateChangedListeners creatureTypeChangedListeners) {
		decreaseHitPoints(worldObject, this, 5 * Item.COMBAT_MULTIPLIER, world);
		
		List<WorldObject> flammableAdjacentWorldObjects = world.findWorldObjects(w -> Reach.distance(worldObject, w) <= 2 && w.hasProperty(Constants.FLAMMABLE) && w.getProperty(Constants.FLAMMABLE));
		for(WorldObject flammableAdjacentWorldObject : flammableAdjacentWorldObjects) {
			if (!flammableAdjacentWorldObject.getProperty(Constants.CONDITIONS).hasCondition(BURNING_CONDITION)) {
				Conditions.add(flammableAdjacentWorldObject, BURNING_CONDITION, 100, world);
			}
		}
	}

	@Override
	public boolean isDisease() {
		return false;
	}

	@Override
	public void conditionEnds(WorldObject worldObject) {
	}

	@Override
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world) {
	}

	@Override
	public String getDeathDescription() {
		return "burned to death";
	}
	
	@Override
	public boolean isMagicEffect() {
		return false;
	}
}
